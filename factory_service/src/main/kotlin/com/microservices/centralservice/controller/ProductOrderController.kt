package com.microservices.centralservice.controller

import com.microservices.centralservice.model.ProductOrder
import com.microservices.centralservice.service.ProductOrderService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController("ProductOrderController")
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