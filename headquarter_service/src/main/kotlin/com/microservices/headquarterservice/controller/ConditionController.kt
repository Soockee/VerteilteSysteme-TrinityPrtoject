package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.service.ConditionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController("ConditionController")
class ConditionController(
        private val conditionService: ConditionService,

        ) {
    @PostMapping("/condition/")
    fun create(
        @RequestBody condition: Condition
    ): Mono<Condition> {
        val conditionResult: Mono<Condition> = conditionService.create(condition)
        val conditionResponse = ConditionResponse(condition.conditions_id, condition.supplier_id, condition.price, condition.negotiation_timestamp)
        conditionService.send(conditionResponse)
        return conditionResult
    }
    @GetMapping("/conditions/")
    fun getAll(
            @RequestBody condition: Condition
    ): Flux<Condition> {
        return conditionService.getAll()
    }
}
