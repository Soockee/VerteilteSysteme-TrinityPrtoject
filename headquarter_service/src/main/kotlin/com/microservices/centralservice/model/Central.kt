package com.microservices.centralservice.model

import org.springframework.data.annotation.Id
import java.util.UUID
import java.sql.Timestamp
import java.sql.Date

data class Central(
    @Id var id: UUID?,
    var name: String,
    var description: String?
)

data class Condition(
    @Id var conditionsId: UUID?,
    var supplierId: UUID,
    var price: String,
    var negotioationTimestamp: Date,
)
data class ConditionResponse(
    @Id var conditionsId: UUID?,
    var supplierId: UUID,
    var price: String,
    var negotioationTimestamp: Date,
)