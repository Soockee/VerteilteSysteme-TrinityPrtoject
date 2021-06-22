package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.Product
import com.microservices.headquarterservice.persistence.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val repository: ProductRepository,
){
    fun create(product: Product): Mono<Product> {
        return repository.save(product)
    }

    fun getAll(): Flux<Product> {
        return repository.findAll()
    }
}