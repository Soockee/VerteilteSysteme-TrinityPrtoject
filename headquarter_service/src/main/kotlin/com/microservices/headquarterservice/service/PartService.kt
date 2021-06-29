package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.common.Part
import com.microservices.headquarterservice.persistence.PartRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PartService(
    private val repository: PartRepository,
){
    /**
     * create a part
     * @param part A part to be saved.
     * @return Returns saved Part.
     */
    fun create(part: Part): Mono<Part> {
        return repository.save(part)
    }
    /**
     *  get all parts
     * @return Returns all Parts.
     */
    fun getAll(): Flux<Part> {
        return repository.findAll()
    }
}