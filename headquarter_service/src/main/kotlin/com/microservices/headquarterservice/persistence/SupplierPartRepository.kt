package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.SupplierPart
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SupplierPartRepository : ReactiveCrudRepository<SupplierPart, UUID>