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
    @PostMapping("/condition/")
    fun create(@RequestBody condition: Condition): Mono<Condition> {
        return conditionService.createConditionAndSend(condition)
    }

    @GetMapping("/condition/")
    fun get(@RequestParam partId: String): Flux<Condition> {
        return conditionService.getByPartId(partId)
    }

    @GetMapping("/conditions/")
    fun getAll(): Flux<Condition> {
        return conditionService.getAll()
    }
}
