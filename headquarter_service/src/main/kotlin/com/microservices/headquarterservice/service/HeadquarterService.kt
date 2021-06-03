package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.exception.NotFoundException
import com.microservices.headquarterservice.model.Headquarter
import com.microservices.headquarterservice.persistence.HeadquarterRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class HeadquarterService(
    private val repository: HeadquarterRepository
) {
    fun create(headquarter: Headquarter): Mono<Headquarter> {
        return repository.save(headquarter)
    }

    fun get(id: UUID): Mono<Headquarter> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
    }

    fun update(
            id: UUID,
            headquarter: Headquarter
    ): Mono<Headquarter> {
        return Mono.just(headquarter)
            .filter { i -> i.id == id }
            .switchIfEmpty(Mono.error(BadRequestException("Given IDs do not match")))
            .flatMap { _ ->
                repository.findById(id)
            }
            .switchIfEmpty(Mono.error(NotFoundException()))
            .map { i ->
                i.name = headquarter.name
                i.description = headquarter.description
                i
            }
            .flatMap { i ->
                repository.save(i)
            }
    }

    fun delete(
        id: UUID
    ): Mono<Headquarter> {
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
