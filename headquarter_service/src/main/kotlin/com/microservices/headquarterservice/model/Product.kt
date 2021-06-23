package com.microservices.headquarterservice.model

import java.util.*
import org.springframework.data.annotation.Id

data class Product(
        @Id var product_id: UUID?,
        var name: String,
        var productionTime: Int,
        var parts: Map<Part, Int>, // part_id, count
)
