package com.microservices.headquarterservice.model.headquarter

import java.util.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("orders")
class Order(
    @Id var order_id: UUID?,
    var customer_id: UUID,
    var begin_order: Instant?,
    var status: String,
)
