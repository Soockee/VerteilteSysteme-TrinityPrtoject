package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.Supplier
import com.microservices.headquarterservice.model.supplier.*
import com.microservices.headquarterservice.service.SupplierService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController("SupplierController")
class SupplierController(
    private val supplierService: SupplierService,
) {
    companion object {
        val logger = LoggerFactory.getLogger(SupplierController::class.java)
    }

    @GetMapping("/supplier/order")
    fun getAll(): Flux<Supplier> {
        logger.info("GET Request: \"/supplier\"")
        return supplierService.getAll()
    }

    @PostMapping("/supplier/order")
    fun createOrder(@RequestBody supplierOrderRequest: SupplierOrderRequest): Mono<SupplierOrderResponse> {
        logger.info("POST Request: \"/supplier\": ${supplierOrderRequest}")
        return supplierService.createOrderByRequest(supplierOrderRequest)
    }

    @GetMapping("/supplier/order/{id}")
    fun getAllSupplierOrder(@PathVariable id: String): SupplierOrderStatusResponse {
        logger.info("GET Request: \"/supplier-order\"/${id}")
        return supplierService.getOrderStatus(UUID.fromString(id)).block()!!
    }

}