package com.microservices.centralservice.service

import com.microservices.centralservice.exception.BadRequestException
import com.microservices.centralservice.exception.NotFoundException
import com.microservices.centralservice.model.Central
import com.microservices.centralservice.persistence.CentralRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class CentralService(
    private val repository: CentralRepository
) {
    fun create(central: Central): Mono<Central> {
        return repository.save(central)
    }

    fun get(id: UUID): Mono<Central> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
    }

    fun update(
            id: UUID,
            central: Central
    ): Mono<Central> {
        return Mono.just(central)
            .filter { i -> i.id == id }
            .switchIfEmpty(Mono.error(BadRequestException("Given IDs do not match")))
            .flatMap { _ ->
                repository.findById(id)
            }
            .switchIfEmpty(Mono.error(NotFoundException()))
            .map { i ->
                i.name = central.name
                i.description = central.description
                i
            }
            .flatMap { i ->
                repository.save(i)
            }
    }

    fun delete(
        id: UUID
    ): Mono<Central> {
        return repository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException()))
            .flatMap { i ->
                repository
                    .delete(i)
                    .thenReturn(i)
            }
    }
    fun getConditions(): Mono<ConditionResponse>{
        var conditions = repository.findAll()
    }
}
