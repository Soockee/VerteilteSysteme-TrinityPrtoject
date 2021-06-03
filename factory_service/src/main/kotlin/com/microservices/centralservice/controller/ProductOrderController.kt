package com.microservices.centralservice.controller

import com.microservices.centralservice.model.ProductOrder
import com.microservices.centralservice.service.ProductOrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono
import java.util.*

class ProductOrderController(private val service: ProductOrderService) {
    @PostMapping("/productOrder/")
    fun create(
        @RequestBody productOrder: ProductOrder
    ): Mono<ProductOrder> {
        return service.create(productOrder)
    }

    @GetMapping("/productOrder/{id}")
    fun get(
        @PathVariable id: UUID
    ): Mono<ProductOrder> {
        return service.get(id)
    }
}