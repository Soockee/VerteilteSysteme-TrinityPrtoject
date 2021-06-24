package com.microservices.headquarterservice.model

import java.util.*
import org.springframework.data.annotation.Id
import java.sql.Timestamp

class Order(
    @Id var order_id: UUID?,
    var customer_id: UUID,
    var begin_order: Timestamp?,
    var status: String,
)
