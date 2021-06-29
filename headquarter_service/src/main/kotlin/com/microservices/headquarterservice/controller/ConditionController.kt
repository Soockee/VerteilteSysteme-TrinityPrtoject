package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.condition.Condition
import com.microservices.headquarterservice.service.ConditionService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.slf4j.LoggerFactory;

@RestController("ConditionController")
class ConditionController(
    private val conditionService: ConditionService,
) {
    companion object {
        val logger = LoggerFactory.getLogger(ConditionController::class.java)
    }

    @PostMapping("/condition")
    fun create(@RequestBody condition: Condition): Mono<Condition> {
        logger.info("POST Request: \"/condition\": ${condition}")
        return conditionService.createConditionAndSend(condition)
    }

    @GetMapping("/condition/{partId}")
    fun get(@PathVariable partId: String): Flux<Condition> {
        logger.info("GET Request: \"/condition\"/${partId}")
        val conditions = conditionService.getByPartId(partId)
        return conditions
    }

    @GetMapping("/condition")
    fun getAll(): Flux<Condition> {
        logger.info("GET Request: \"/condition\"")
        return conditionService.getAll()
    }
}
