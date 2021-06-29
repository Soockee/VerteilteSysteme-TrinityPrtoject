package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.Product
import com.microservices.headquarterservice.model.headquarter.ProductPart
import com.microservices.headquarterservice.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController("ProductController")
class ProductController(
    private val productService: ProductService,
) {
    companion object {
        val logger = LoggerFactory.getLogger(ProductController::class.java)
    }

    @PostMapping("/product")
    fun create(@RequestBody product: Product): Mono<Product> {
        logger.info("POST Request: \"/product\": $product")
        return productService.create(product)
    }

    @GetMapping("/product/{id}")
    fun get(@PathVariable id: String): Mono<Void> {
        logger.info("POST Request: \"/product\"$id")
        productService.getProductResponse(UUID.fromString(id))
        return Mono.empty()
    }

    @GetMapping("/product")
    fun getAll(): Flux<Product> {
        logger.info("GET Request: \"/product\"")
        return productService.getAll()
    }
}