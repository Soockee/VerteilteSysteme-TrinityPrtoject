package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.model.Part
import com.microservices.headquarterservice.persistence.ConditionRepository
import java.sql.Timestamp
import java.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ConditionService(
        private val repository: ConditionRepository,
        private val partService: PartService,
        private val supplierPartService: SupplierPartService,
        private val supplierService: SupplierService,
        private val rabbitTemplate: AmqpTemplate,
        @Value("\${microservice.rabbitmq.routingkey}") val headquarterRoutingKey: String,
        @Value("\${microservice.rabbitmq.queue}") val headquarterQueueName: String,
        @Value("\${microservice.rabbitmq.exchange}") val headquarterExchangeName: String,
) {
    companion object {
        val logger = LoggerFactory.getLogger(ConditionService::class.java)
    }

    fun create(condition: Condition): Mono<Condition> {
        // supplier_id where does it come from
        // condition.supplier_id = ???
        condition.negotiation_timestamp = Timestamp(System.currentTimeMillis())
        return repository.save(condition)
    }

    fun getAll(): Flux<Condition> {
        val conditions = repository.findAll()
        return conditions
    }

    fun getByPartId(partId: String): Flux<Condition> {
        var condition: Flux<Condition> =
                repository.findAll().filter { elem ->
                    elem.part_supplier_id.toString().equals(partId)
                }
        
        var conditionResponse = ConditionResponse(UUID.fromString(partId), mutableListOf())
        var conditionList: MutableList<Condition>? = condition.collectList().block()
        if (!conditionList.isNullOrEmpty()) {
            conditionResponse.conditions = conditionList
            rabbitTemplate.convertAndSend(
                    headquarterExchangeName,
                    headquarterRoutingKey,
                    Json.encodeToString(conditionResponse))
        }
        return condition
    }

    // fun getConditionResponseByPartName(part_name: String): Flux<Condition> {
    //     var parts: Flux<Part> =
    //             partService.getAll().filter { elem -> elem.part_id.toString().equals(partId) }
    //     var condition: Flux<Condition> =
    //             repository.findAll().filter { elem ->
    //                 elem.part_supplier_id.toString().equals(partId)
    //             }
    //     var conditionResponse = ConditionResponse(UUID.fromString(partId), mutableListOf())
    //     var conditionList: MutableList<Condition>? = condition.collectList().block()
    //     if (!conditionList.isNullOrEmpty()) {
    //         conditionResponse.conditions = conditionList
    //         rabbitTemplate.convertAndSend(
    //                 headquarterExchangeName,
    //                 headquarterRoutingKey,
    //                 Json.encodeToString(conditionResponse))
    //     }
    //     return condition
    // }

    fun send(condition: ConditionResponse) {
        rabbitTemplate.convertAndSend(
                headquarterExchangeName, headquarterRoutingKey, Json.encodeToString(condition))
        logger.debug("Send msg = " + condition)
    }
}
