package com.microservices.centralservice.service

import com.microservices.centralservice.exception.NotFoundException
import com.microservices.centralservice.model.ProductOrder
import com.microservices.centralservice.persistence.ProductOrderRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class ProductOrderService(
    private val repository: ProductOrderRepository
) {
    fun create(productOrder: ProductOrder): Mono<ProductOrder> {
        return repository.save(productOrder)
    }

    fun get(id: UUID): Mono<ProductOrder> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
    }
}