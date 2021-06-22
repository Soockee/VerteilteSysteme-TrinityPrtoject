package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.service.ConditionService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
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
        val conditionResponse = ConditionResponse(condition.conditions_id, condition.supplier_id, condition.part_id,
            condition.price, condition.negotiation_timestamp)
        conditionService.send(conditionResponse)
        return conditionResult
    }

    /*@GetMapping("/condition/{partId}")
    fun get(
        @RequestParam partId: String
    ): Mono<ConditionResponse> {

        return conditionService.getByPartId(partId)
    }*/

    @GetMapping( "/conditions/", MediaType.APPLICATION_JSON_VALUE)
    fun getAll(
    ): Flux<Condition> {
        System.out.println("Logg conditions request.")
        return conditionService.getAll()
    }
}
