package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.Supplier
import com.microservices.headquarterservice.model.supplier.*
import com.microservices.headquarterservice.service.SupplierService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController("SupplierController")
class SupplierController(
    private val supplierService: SupplierService,
) {

    @GetMapping("/suppliers/")
    fun getAll(
    ): Flux<Supplier> {
        return supplierService.getAll()
    }

    @PostMapping("/supplier")
    fun createOrder(
        @RequestBody supplierOrderRequest: SupplierOrderRequest
    ): Mono<SupplierOrderResponse> {
        return supplierService.createOrderByRequest(supplierOrderRequest)
    }

    @GetMapping("/supplier-orders/")
    fun getAllSupplierOrder(
    ): Flux<SupplierOrder> {
        return supplierService.getAllOrders()
    }

    @GetMapping("/supplier-orders-parts/")
    fun getAllSupplierOrderParts(
    ): Flux<SupplierOrderPart> {
        return supplierService.getAllOrderParts()
    }

    @GetMapping("/supplier-order/")
    fun getAllSupplierOrder(
        @RequestParam order_id: String
    ): SupplierOrderStatusResponse {
        return supplierService.getOrderStatus(UUID.fromString(order_id)).block()!!
    }

}