package com.microservices.centralservice.controller

import com.microservices.centralservice.model.Central
import com.microservices.centralservice.service.CentralService
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

@RestController("CentralController")
class CentralController(
        private val centralService: CentralService,
        val template: RabbitTemplate,

        ) {
    @PostMapping("/central/")
    fun create(
        @RequestBody central: Central
    ): Mono<Central> {
        val queue: Queue = Queue("central")
        template.convertAndSend(queue.getName(), "kekw")
        return centralService.create(central)
    }

    @GetMapping("/central/{id}")
    fun get(
        @PathVariable id: UUID
    ): Mono<Central> {
        return centralService.get(id)
    }

    @PutMapping("/central/{id}")
    fun put(
        @PathVariable id: UUID,
        @RequestBody central: Central
    ): Mono<Central> {
        return centralService.update(id, central)
    }

    @DeleteMapping("/central/{id}")
    fun delete(
        @PathVariable id: UUID
    ): Mono<Central> {
        return centralService.delete(id)
    }
}
