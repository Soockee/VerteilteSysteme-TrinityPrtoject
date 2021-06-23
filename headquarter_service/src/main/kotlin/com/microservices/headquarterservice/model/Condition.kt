@file:UseSerializers(UUIDSerializer::class, BigDecimalSerializer::class, TimestampSerializer::class)

package com.microservices.headquarterservice.model

import com.microservices.headquarterservice.serializer.*
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import java.util.UUID
import kotlinx.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.springframework.data.annotation.*
import org.springframework.data.annotation.Id

@Serializable
@SerialName("Condition")
class Condition(
                @Id var conditions_id: UUID,
                var supplier_id: UUID,
                var part_supplier_id: UUID,
                var price: BigDecimal,
                var currency: String,
                var negotiation_timestamp: Timestamp,
)
