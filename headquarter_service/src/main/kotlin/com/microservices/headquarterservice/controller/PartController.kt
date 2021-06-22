package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.Part
import com.microservices.headquarterservice.service.PartService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController("PartController")
class PartController (
private val partService: PartService,

) {
    @PostMapping("/part/")
    fun create(
        @RequestBody part: Part
    ): Mono<Part> {
        return partService.create(part)
    }

    @GetMapping( "/parts/")
    fun getAll(
    ): Flux<Part> {
        return partService.getAll()
    }
}