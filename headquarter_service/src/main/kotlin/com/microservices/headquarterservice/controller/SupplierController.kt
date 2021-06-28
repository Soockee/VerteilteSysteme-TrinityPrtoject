package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.Condition
import com.microservices.headquarterservice.model.headquarter.Product
import com.microservices.headquarterservice.model.headquarter.ProductPart
import com.microservices.headquarterservice.model.headquarter.Supplier
import com.microservices.headquarterservice.model.supplier.SupplierOrder
import com.microservices.headquarterservice.model.supplier.SupplierOrderRequest
import com.microservices.headquarterservice.model.supplier.SupplierOrderResponse
import com.microservices.headquarterservice.service.ProductService
import com.microservices.headquarterservice.service.SupplierService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController("SupplierController")
class SupplierController (
    private val supplierService: SupplierService,
    ) {

    @GetMapping( "/suppliers/")
    fun getAll(
    ): Flux<Supplier> {
        return supplierService.getAll()
    }
    @PostMapping( "/supplier")
    fun createOrder(
        @RequestBody supplierOrderRequest: SupplierOrderRequest
    ): Mono<SupplierOrderResponse> {
        return supplierService.createOrderByRequest(supplierOrderRequest)
    }
    @GetMapping( "/supplier-orders/")
    fun getAllSupplierOrder(
    ): Flux<SupplierOrder> {
        return supplierService.getAllOrders()
    }
}