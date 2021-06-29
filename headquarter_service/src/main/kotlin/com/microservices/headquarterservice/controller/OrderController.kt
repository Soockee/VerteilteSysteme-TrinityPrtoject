package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.order.Order
import com.microservices.headquarterservice.model.headquarter.order.OrderProduct
import com.microservices.headquarterservice.model.headquarter.order.OrderRequest
import com.microservices.headquarterservice.service.ConditionService
import com.microservices.headquarterservice.service.OrderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController("OrderController")
class OrderController(
    private val orderService: OrderService,
) {
    val logger: Logger = LoggerFactory.getLogger(OrderController::class.java)

    @PostMapping("/order")
    fun create(@RequestBody orderRequest: OrderRequest): Order {
        logger.info("POST Request: \"/order\": ${orderRequest}")
        return orderService.createOrder(orderRequest)
    }

    @GetMapping("/order")
    fun getAll(): Flux<Order> {
        logger.info("GET Request: \"/order\"")
        return orderService.getAllOrders()
    }

    @GetMapping("/order/status")
    fun getStatusByOrderId(@RequestParam orderId: String): Mono<String> {
        logger.info("GET Request: \"/order\": $orderId")
        return orderService.getStatusById(UUID.fromString(orderId))
    }
}