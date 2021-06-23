@file:UseSerializers(UUIDSerializer::class, BigDecimalSerializer::class, TimestampSerializer::class)

package com.microservices.headquarterservice.model

import java.math.BigDecimal
import java.sql.Timestamp
import java.text.DecimalFormat
import java.util.*
import java.util.UUID
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.springframework.data.annotation.*
import org.springframework.data.annotation.Id

@Serializable()
data class Condition(
                @Id var conditions_id: UUID,
                var supplier_id: UUID,
                var part_supplier_id: UUID,
                var price: BigDecimal,
                var currency: String,
                var negotiation_timestamp: Timestamp,
)

object UUIDSerializer : KSerializer<UUID> {
        override fun deserialize(decoder: Decoder): UUID {
                return UUID.fromString(decoder.decodeString())
        }

        override val descriptor: SerialDescriptor =
                        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: UUID) {
                return encoder.encodeString(value.toString())
        }
}

object BigDecimalSerializer : KSerializer<BigDecimal> {
        override fun deserialize(decoder: Decoder): BigDecimal {
                val df = DecimalFormat()
                df.isParseBigDecimal = true
                return df.parse(decoder.decodeString()) as BigDecimal
        }

        override val descriptor: SerialDescriptor =
                        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: BigDecimal) {
                return encoder.encodeString(value.toString())
        }
}

object TimestampSerializer : KSerializer<Timestamp> {
        override fun deserialize(decoder: Decoder): Timestamp {
                return Timestamp.valueOf(decoder.decodeString())
        }

        override val descriptor: SerialDescriptor =
                        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Timestamp) {
                return encoder.encodeString(value.toString())
        }
}
