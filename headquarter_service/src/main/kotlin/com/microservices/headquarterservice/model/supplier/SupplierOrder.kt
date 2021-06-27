package com.microservices.headquarterservice.model.supplier

import java.util.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("supplier_order")
class SupplierOrder(
    @Id var order_id: UUID,
    var supplier_id: UUID,
    var factory_id: UUID,
    var begin_order: Instant?,
    var negotiation_timestamp: Instant?,
    var status: String,
)
