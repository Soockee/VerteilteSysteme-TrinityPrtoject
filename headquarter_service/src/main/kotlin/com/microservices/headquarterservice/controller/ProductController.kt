package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.Product
import com.microservices.headquarterservice.model.headquarter.ProductPart
import com.microservices.headquarterservice.service.ProductService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

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

    @GetMapping("/product/")
    fun get(@RequestParam productId: String): Mono<Void> {
        productService.getProductResponse(UUID.fromString(productId))
        return Mono.empty()
    }

    @GetMapping("/product-parts/")
    fun getProductParts(@RequestParam productId: String): Flux<ProductPart> {
        return productService.getProductPartsByProductId(UUID.fromString(productId))
    }

    @GetMapping("/product-parts/all/")
    fun getAllProductParts():  Flux<ProductPart> {
        return productService.getAllProductParts()
    }

    @GetMapping( "/products/")
    fun getAll(
    ): Flux<Product> {
        return productService.getAll()
    }
}