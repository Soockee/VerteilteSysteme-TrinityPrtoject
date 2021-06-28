package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.common.Part
import com.microservices.headquarterservice.persistence.PartRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PartService(
    private val repository: PartRepository,
){
    fun create(part: Part): Mono<Part> {
        return repository.save(part)
    }

    fun getAll(): Flux<Part> {
        return repository.findAll()
    }
}