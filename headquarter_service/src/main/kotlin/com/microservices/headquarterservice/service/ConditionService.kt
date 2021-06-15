package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.exception.NotFoundException
import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.persistence.ConditionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

import org.springframework.beans.factory.annotation.Value
import org.springframework.amqp.core.AmqpTemplate
import com.microservices.headquarterservice.model.ConditionResponse
import reactor.core.publisher.Flux
import java.sql.Timestamp

@Service
class ConditionService(
    private val repository: ConditionRepository,
    private val rabbitTemplate: AmqpTemplate,
){

    @Value("${microservice.rabbitmq.queue}")
	val headquarterQueueName: String? = null

	@Value("${microservice.rabbitmq.exchange}")
	val headquarterExchangeName: String? = null
	
    @Value("${microservice.rabbitmq.routingkey}")
	val headquarterRoutingKey: String? = null

    fun create(condition: Condition): Mono<Condition> {
        // supplier_id where does it come from
        // condition.supplier_id = ???
        condition.negotiation_timestamp = Timestamp(System.currentTimeMillis());
        return repository.save(condition)
    }
    fun getAll(): Flux<Condition> {
        return repository.findAll()
    }

    fun send(condition: ConditionResponse) {
		rabbitTemplate.convertAndSend(headquarterExchangeName, headquarterRoutingKey, condition);
		System.out.println("Send msg = " + condition);
	}

}