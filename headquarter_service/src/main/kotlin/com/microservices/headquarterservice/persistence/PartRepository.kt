package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.common.Part
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PartRepository : ReactiveCrudRepository<Part, UUID>