package com.microservices.headquarterservice.model.common

import java.util.*
import org.springframework.data.annotation.Id

data class Part(
                @Id var part_id: UUID,
                var name: String,
                var delievery_time: Int
)
