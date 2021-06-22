package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.*

data class Product(
    @Id var product_id: UUID?,
    var name: String,
    var productionTime: Integer,
    var parts: Map<UUID, Integer>, // part_id, count
)
