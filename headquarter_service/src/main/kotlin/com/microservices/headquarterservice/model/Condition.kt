package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.UUID
import java.sql.Timestamp
import java.sql.Date

data class Condition(
    @Id var conditionsId: UUID?,
    var supplierId: UUID,
    var price: String,
    var negotioationTimestamp: Date,
)

