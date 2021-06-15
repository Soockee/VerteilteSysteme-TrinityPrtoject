package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.exception.NotFoundException
import com.microservices.headquarterservice.model.Headquarter


import com.microservices.headquarterservice.persistence.HeadquarterRepository
import org.springframework.stereotype.Service
import org.springframework.amqp.core.AmqpTemplate
import reactor.core.publisher.Mono
import java.util.UUID
import org.springframework.beans.factory.annotation.Value

@Service
class HeadquarterService(
    private val repository: HeadquarterRepository,
    private val rabbitTemplate: AmqpTemplate,
) {

    @Value("${microservice.rabbitmq.queue}")
	val headquarterQueueName: String? = null

	@Value("${microservice.rabbitmq.exchange}")
	val headquarterExchangeName: String? = null
	
    @Value("${microservice.rabbitmq.routingkey}")
	val headquarterRoutingKey: String? = null

    fun create(headquarter: Headquarter): Mono<Headquarter> {
        return repository.save(headquarter)
    }

    fun get(id: UUID): Mono<Headquarter> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
    }

    fun update(
            id: UUID,
            headquarter: Headquarter
    ): Mono<Headquarter> {
        return Mono.just(headquarter)
            .filter { i -> i.id == id }
            .switchIfEmpty(Mono.error(BadRequestException("Given IDs do not match")))
            .flatMap { _ ->
                repository.findById(id)
            }
            .switchIfEmpty(Mono.error(NotFoundException()))
            .map { i ->
                i.name = headquarter.name
                i.description = headquarter.description
                i
            }
            .flatMap { i ->
                repository.save(i)
            }
    }

    fun delete(
        id: UUID
    ): Mono<Headquarter> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
            .flatMap { i ->
                repository
                    .delete(i)
                    .thenReturn(i)
            }
    }


    
}
