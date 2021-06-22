package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.Part
import com.microservices.headquarterservice.model.Product
import com.microservices.headquarterservice.service.PartService
import com.microservices.headquarterservice.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController("ProductController")
class ProductController (
    private val productService: ProductService,

    ) {
    @PostMapping("/product/")
    fun create(
        @RequestBody product: Product
    ): Mono<Product> {
        return productService.create(product)
    }

    @GetMapping( "/products/")
    fun getAll(
    ): Flux<Product> {
        return productService.getAll()
    }
}