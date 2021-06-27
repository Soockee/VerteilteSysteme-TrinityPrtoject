package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.persistence.ConditionRepository
import java.util.*
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

@Service
class ConditionService(
    private val repository: ConditionRepository,
    private val partService: PartService,
    private val supplierService: SupplierService,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.routingkey_condition}") val headquarterRoutingKey: String,
    @Value("\${microservice.rabbitmq.exchange}") val headquarterExchangeName: String,
) {
    companion object {
        val logger = LoggerFactory.getLogger(ConditionService::class.java)
    }

    fun create(condition: Condition): Mono<Condition> {
        condition.negotiation_timestamp = Instant.now()
        return repository.save(condition)
    }

    fun createConditionAndUpdate(condition: Condition): Mono<Condition> {
        val savedCondition = create(condition)
        savedCondition.publishOn(Schedulers.boundedElastic())
            .map { conditionItem ->
                send(ConditionResponse(condition.part_id, mutableListOf(conditionItem))) }
            .toFuture()
        return savedCondition
    }

    fun getAll(): Flux<Condition> {
        val conditions = repository.findAll()
        return conditions
    }

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

    fun send(condition: ConditionResponse) {
        rabbitTemplate.convertAndSend(
            headquarterExchangeName, headquarterRoutingKey, Json.encodeToString(condition)
        )
        logger.info("Send msg = " + condition)
    }
}
