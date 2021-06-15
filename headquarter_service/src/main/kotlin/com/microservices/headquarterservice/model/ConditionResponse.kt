package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.UUID
import java.math.BigDecimal
import java.sql.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class ConditionResponse(
    @Id var conditions_id: UUID?,
    var supplier_id: UUID?,
    var price: BigDecimal,
    var negotioation_timestamp: Timestamp?,
)