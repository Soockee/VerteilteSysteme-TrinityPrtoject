package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.headquarter.condition.Condition
import com.microservices.headquarterservice.model.headquarter.condition.ConditionResponse
import com.microservices.headquarterservice.persistence.ConditionRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant
import java.util.*

@Service
class ConditionService(
    private val repository: ConditionRepository,
    private val partService: PartService,
    private val supplierService: SupplierService,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.queueCondition}") val headquarterConditionQueue: String,
) {
    val logger = LoggerFactory.getLogger(ConditionService::class.java)

    /**
     * save a condition into db
     * @param condition A condition to be created.
     * @return Returns saved Condition.
     */
    fun create(condition: Condition): Mono<Condition> {
        condition.negotiation_timestamp = Instant.now()
        return repository.save(condition)
    }
    /**
     * save a condition into db and send to queue
     * @param condition A condition to be created.
     * @return Returns saved Condition.
     */
    fun createConditionAndSend(condition: Condition): Mono<Condition> {
        val savedCondition = create(condition)
        savedCondition.publishOn(Schedulers.boundedElastic())
            .map { conditionItem ->
                send(ConditionResponse(condition.part_id, mutableListOf(conditionItem))) }
            .toFuture()
        return savedCondition
    }
    /**
     * return all conditions
     */
    fun getAll(): Flux<Condition> {
        val conditions = repository.findAll()
        return conditions
    }
    /**
     * return N conditions by providing a part id and send to queue
     * @param partId A part id.
     * @return Returns all retrieved Condition.
     */
    fun getByPartId(partId: String): Flux<Condition> {
        var condition = repository.findAll()
            .filter { elem ->
                elem.part_id.toString() == partId
            }
            .replay()
            .autoConnect()
        condition
            .publishOn(Schedulers.boundedElastic())
            .collectList()
            .doOnNext { conditionList ->
                send(ConditionResponse(UUID.fromString(partId), conditionList))
            }
            .toFuture()
        return condition
    }
    /**
     * send a condition response
     */
    fun send(condition: ConditionResponse) {
        rabbitTemplate.convertAndSend(
            headquarterConditionQueue, Json.encodeToString(condition)
        )
        logger.info("Send msg = " + condition)
    }
}
