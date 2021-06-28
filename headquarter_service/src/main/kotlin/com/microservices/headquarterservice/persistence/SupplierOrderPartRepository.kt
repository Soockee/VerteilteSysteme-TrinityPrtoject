package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.supplier.SupplierOrderPart
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SupplierOrderPartRepository : ReactiveCrudRepository<SupplierOrderPart, UUID>