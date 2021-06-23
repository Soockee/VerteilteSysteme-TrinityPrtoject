package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.service.ConditionService
import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.slf4j.Logger;
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
        val conditionResult: Mono<Condition> = conditionService.create(condition)
        // val conditionResponse = ConditionResponse(condition.conditionsId)
        // conditionService.send(conditionResponse)
        return conditionResult
    }

    @GetMapping("/condition/")
    fun get(@RequestParam partId: String): Flux<Condition> {
        val conditions = conditionService.getByPartId(partId)

        conditions.subscribe(this::printCondition)
        // val response  = conditions.flatMap(con -> buildConditionResponse(con))
        return conditions
        // conditionService.send(conditionResponse)
    }

    @GetMapping("/conditions/")
    fun getAll(): Flux<Condition> {
        logger.warn("Log conditions request")
        return conditionService.getAll()
    }

    fun buildConditionResponse(conditions: Flux<Condition>, partId: String): Mono<Void> {
        val conditionResponse = ConditionResponse(UUID.fromString(partId), mutableListOf())
        return Mono.empty()
    }

    fun printCondition(condition: Condition){
        val encode = Json.encodeToString(condition)
        val decode = Json.decodeFromString<Condition>(encode)

        logger.warn("Condition Encode is" + encode)
    }
}
