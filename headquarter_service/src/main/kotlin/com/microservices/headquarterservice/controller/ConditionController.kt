package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.headquarter.Condition
import com.microservices.headquarterservice.service.ConditionService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.slf4j.LoggerFactory;

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@RestController("ConditionController")
class ConditionController(
        private val conditionService: ConditionService,
) {
    companion object {
        val logger = LoggerFactory.getLogger(ConditionService::class.java)
    }
    @PostMapping("/condition/")
    fun create(@RequestBody condition: Condition): Mono<Condition> {
        return conditionService.createConditionAndUpdate(condition)
    }

    @GetMapping("/condition/")
    fun get(@RequestParam partId: String): Flux<Condition> {
        val conditions = conditionService.getByPartId(partId)

        conditions.subscribe(this::printCondition)
        return conditions
    }

    @GetMapping("/conditions/")
    fun getAll(): Flux<Condition> {
        logger.warn("Log conditions request")
        return conditionService.getAll()
    }

    fun printCondition(condition: Condition){
        val encode = Json.encodeToString(condition)
        val decode = Json.decodeFromString<Condition>(encode)

        logger.warn("Condition Encode is" + encode)
    }
}
