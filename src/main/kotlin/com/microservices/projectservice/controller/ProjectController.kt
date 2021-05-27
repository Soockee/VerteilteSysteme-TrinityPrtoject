package com.microservices.projectservice.controller

import com.microservices.projectservice.model.Project
import com.microservices.projectservice.service.ProjectService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import org.springframework.amqp.core.Queue 
import java.util.UUID

@RestController("ProjectController")
class ProjectController(
    private val projectService: ProjectService,
    val template: RabbitTemplate,
    
) {
    @PostMapping("/project/")
    fun create(
        @RequestBody project: Project
    ): Mono<Project> {
        val queue: Queue = Queue("projects")
        template.convertAndSend(queue.getName(), "kekw")
        return projectService.create(project)
    }

    @GetMapping("/project/{id}")
    fun get(
        @PathVariable id: UUID
    ): Mono<Project> {
        return projectService.get(id)
    }

    @PutMapping("/project/{id}")
    fun put(
        @PathVariable id: UUID,
        @RequestBody project: Project
    ): Mono<Project> {
        return projectService.update(id, project)
    }

    @DeleteMapping("/project/{id}")
    fun delete(
        @PathVariable id: UUID
    ): Mono<Project> {
        return projectService.delete(id)
    }
}
