package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.exception.NotFoundException
import com.microservices.headquarterservice.model.Headquarter

import com.microservices.headquarterservice.persistence.HeadquarterRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

import com.microservices.headquarterservice.model.ConditionResponse
@Service
class ConditionService(
    private val repository: HeadquarterRepository
){


    //fun getConditions(): Mono<ConditionResponse>{
    //    var conditions = repository.findAll()
    //}
}