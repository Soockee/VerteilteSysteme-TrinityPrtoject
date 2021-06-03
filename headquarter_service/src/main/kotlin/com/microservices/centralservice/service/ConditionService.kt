package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.exception.NotFoundException
import com.microservices.headquarterservice.model.Central

import com.microservices.headquarterservice.persistence.CentralRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

import com.microservices.headquarterservice.model.ConditionResponse
@Service
class CentralService(
    private val repository: HeadquarterRepository
){


    fun getConditions(): Mono<ConditionResponse>{
        var conditions = repository.findAll()
        return null;
    }
}