package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.util.UUID
import java.sql.Timestamp

data class Condition(
        @Id var conditions_id: UUID?,
        var supplier_id: UUID?,
        var price: BigDecimal,
        var negotiation_timestamp: Timestamp?,
)

