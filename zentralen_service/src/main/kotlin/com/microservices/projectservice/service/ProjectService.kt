package com.microservices.projectservice.service

import com.microservices.projectservice.exception.BadRequestException
import com.microservices.projectservice.exception.NotFoundException
import com.microservices.projectservice.model.Project
import com.microservices.projectservice.persistence.ProjectRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class ProjectService(
    private val repository: ProjectRepository
) {
    fun create(project: Project): Mono<Project> {
        return repository.save(project)
    }

    fun get(id: UUID): Mono<Project> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
    }

    fun update(
        id: UUID,
        project: Project
    ): Mono<Project> {
        return Mono.just(project)
            .filter { i -> i.id == id }
            .switchIfEmpty(Mono.error(BadRequestException("Given IDs do not match")))
            .flatMap { _ ->
                repository.findById(id)
            }
            .switchIfEmpty(Mono.error(NotFoundException()))
            .map { i ->
                i.name = project.name
                i.description = project.description
                i
            }
            .flatMap { i ->
                repository.save(i)
            }
    }

    fun delete(
        id: UUID
    ): Mono<Project> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
            .flatMap { i ->
                repository
                    .delete(i)
                    .thenReturn(i)
            }
    }
}
