@file:UseSerializers(UUIDSerializer::class, BigDecimalSerializer::class, InstantSerializer::class)

package com.microservices.headquarterservice.model.headquarter.condition

import com.fasterxml.jackson.annotation.JsonProperty
import com.microservices.headquarterservice.serializer.*
import java.math.BigDecimal
import java.util.UUID
import kotlinx.serialization.*
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import java.time.Instant

@Serializable
@SerialName("Condition")
class Condition(
    @Id var conditions_id: UUID?,
    var supplier_id: UUID,
    var part_id: UUID,
    var price: BigDecimal,
    var currency: String,
    var negotiation_timestamp: Instant?,
)
