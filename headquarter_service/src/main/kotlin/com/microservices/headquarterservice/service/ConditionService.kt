package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.exception.NotFoundException
import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.persistence.ConditionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

import com.microservices.headquarterservice.model.ConditionResponse
import reactor.core.publisher.Flux
import java.sql.Timestamp

@Service
class ConditionService(
    private val repository: ConditionRepository
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
}