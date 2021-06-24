package com.microservices.headquarterservice.model

import java.util.*
import org.springframework.data.annotation.Id

data class Part(
                @Id var part_id: UUID,
                var name: String,
)
