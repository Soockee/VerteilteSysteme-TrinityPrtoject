package com.microservices.headquarterservice.model

import java.util.*
import org.springframework.data.annotation.Id

data class Supplier(
                @Id var supplier_id: UUID?,
                var name: String,
)
