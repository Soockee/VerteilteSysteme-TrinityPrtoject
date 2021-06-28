package com.microservices.headquarterservice.persistence

import com.microservices.headquarterservice.model.headquarter.Product
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : ReactiveCrudRepository<Product, UUID>