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

@Service
class ConditionService(
    private val repository: ConditionRepository,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.routingkey}")
    val headquarterRoutingKey: String? = null,
    @Value("\${microservice.rabbitmq.queue}")
    val headquarterQueueName: String? = null,
    @Value("\${microservice.rabbitmq.exchange}")
    val headquarterExchangeName: String? = null,
){
    fun create(condition: Condition): Mono<Condition> {
        // supplier_id where does it come from
        // condition.supplier_id = ???
        condition.negotiation_timestamp = Timestamp(System.currentTimeMillis());
        return repository.save(condition)
    }

    fun getAll(): Flux<Condition> {
        return repository.findAll()
    }

    fun getByPartId(partId: String): Flux<Condition> {
        var condition: Flux<Condition> = repository.findAll().filter{ elem -> elem.part_id == UUID.fromString(partId) }
        System.out.println("Ask partId: " + partId + "; result: " + condition)
        return condition
    }

    fun send(condition: ConditionResponse) {
		rabbitTemplate.convertAndSend(headquarterExchangeName, headquarterRoutingKey, Json.encodeToString(condition));
		System.out.println("Send msg = " + condition);
	}

}