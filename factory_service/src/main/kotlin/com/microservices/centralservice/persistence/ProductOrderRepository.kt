package com.microservices.centralservice.persistence

import com.microservices.centralservice.model.ProductOrder
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductOrderRepository : ReactiveCrudRepository<ProductOrder, UUID>