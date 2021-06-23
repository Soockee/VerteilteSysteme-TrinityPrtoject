package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.SupplierPart
import com.microservices.headquarterservice.persistence.SupplierPartRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class SupplierPartService(
    private val repository: SupplierPartRepository,
){
    fun create(part: SupplierPart): Mono<SupplierPart> {
        return repository.save(part)
    }

    fun getAll(): Flux<SupplierPart> {
        return repository.findAll()
    }
}