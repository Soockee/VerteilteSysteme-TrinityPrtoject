package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.persistence.ConditionRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.util.*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
class ConditionService(
    private val repository: ConditionRepository,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.routingkey}")
    val headquarterRoutingKey: String,
    @Value("\${microservice.rabbitmq.queue}")
    val headquarterQueueName: String,
    @Value("\${microservice.rabbitmq.exchange}")
    val headquarterExchangeName: String,
){
    companion object {
        val logger = LoggerFactory.getLogger(ConditionService::class.java)
    }

    fun create(condition: Condition): Mono<Condition> {
        // supplier_id where does it come from
        // condition.supplier_id = ???
        condition.negotiation_timestamp = Timestamp(System.currentTimeMillis());
        return repository.save(condition)
    }

    fun getAll(): Flux<Condition> {
        logger.warn("Log conditions request in service")
        val conditions =  repository.findAll()
        logger.warn("Log conditions request in service done")
        return conditions
    }

    fun getByPartId(partId: String): Flux<Condition> {
        var condition: Flux<Condition> = repository.findAll()
        var filtered_condition = condition.filter{ elem -> elem.part_supplier_id.toString().equals(partId)}
        logger.warn("Ask partId: " + partId + "; result: " + filtered_condition)
        return filtered_condition
    }

    fun send(condition: ConditionResponse) {
		rabbitTemplate.convertAndSend(headquarterExchangeName, headquarterRoutingKey, Json.encodeToString(condition));
        logger.debug("Send msg = " + condition)
	}

}