package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.Supplier
import com.microservices.headquarterservice.persistence.SupplierRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class SupplierService(
    private val repository: SupplierRepository,
){
    fun create(supplier: Supplier): Mono<Supplier> {
        return repository.save(supplier)
    }

    fun getAll(): Flux<Supplier> {
        return repository.findAll()
    }
}