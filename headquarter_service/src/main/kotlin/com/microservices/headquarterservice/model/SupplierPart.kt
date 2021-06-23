package com.microservices.headquarterservice.model

import java.util.*
import org.springframework.data.annotation.Id

data class SupplierPart(
                @Id var supplier_part_id: UUID?,
                var supplier_id: UUID?,
                var part_id: UUID?,
                var name: String,
)
