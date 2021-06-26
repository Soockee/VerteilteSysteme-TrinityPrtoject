package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.OrderProduct
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderProductRepository : ReactiveCrudRepository<OrderProduct, UUID>