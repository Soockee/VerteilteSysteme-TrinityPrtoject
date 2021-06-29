package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.common.Part
import com.microservices.headquarterservice.service.ConditionService
import com.microservices.headquarterservice.service.PartService
import org.slf4j.LoggerFactory
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
    companion object {
        val logger = LoggerFactory.getLogger(PartController::class.java)
    }

    @PostMapping("/part")
    fun create(@RequestBody part: Part): Mono<Part> {
        logger.info("GET Request: \"/part\": $part")
        return partService.create(part)
    }

    @GetMapping( "/part")
    fun getAll(): Flux<Part> {
        logger.info("GET Request: \"/part\"")
        return partService.getAll()
    }
}