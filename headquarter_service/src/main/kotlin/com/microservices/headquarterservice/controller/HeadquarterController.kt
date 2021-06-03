package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.Headquarter
import com.microservices.headquarterservice.model.ConditionResponse
import com.microservices.headquarterservice.service.HeadquarterService
import com.microservices.headquarterservice.service.ConditionService
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController("HeadquarterController")
class HeadquarterController(
        private val headquarterService: HeadquarterService,
        private val conditionService: ConditionService,
        val template: RabbitTemplate,

        ) {
    @PostMapping("/headquarter/")
    fun create(
        @RequestBody headquarter: Headquarter
    ): Mono<Headquarter> {
        val queue: Queue = Queue("central")
        template.convertAndSend(queue.getName(), "kekw")
        return headquarterService.create(headquarter)
    }

    @GetMapping("/central/{id}")
    fun get(
        @PathVariable id: UUID
    ): Mono<Headquarter> {
        return headquarterService.get(id)
    }

    @PutMapping("/headquarter/{id}")
    fun put(
        @PathVariable id: UUID,
        @RequestBody headquarter: Headquarter
    ): Mono<Headquarter> {
        return headquarterService.update(id, headquarter)
    }

    @DeleteMapping("/central/{id}")
    fun delete(
        @PathVariable id: UUID
    ): Mono<Headquarter> {
        return headquarterService.delete(id)
    }

  //  @GetMapping("/condition/")
  //  fun get(): Mono<ConditionResponse> {
   //     return conditionService.getConditions()
  //  }
}
