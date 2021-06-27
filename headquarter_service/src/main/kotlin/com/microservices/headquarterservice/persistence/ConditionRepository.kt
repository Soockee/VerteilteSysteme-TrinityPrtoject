package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.headquarter.Condition
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ConditionRepository : ReactiveCrudRepository<Condition, UUID>
