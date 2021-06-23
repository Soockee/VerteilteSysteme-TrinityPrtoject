package com.microservices.headquarterservice.model

import java.util.*
import org.springframework.data.annotation.Id

data class Part(
                @Id var part_id: UUID,
                var supplier_part_id: UUID,
                var supplier_id: UUID,
)
