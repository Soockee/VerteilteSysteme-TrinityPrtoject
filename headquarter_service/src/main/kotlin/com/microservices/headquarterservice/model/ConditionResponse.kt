package com.microservices.headquarterservice.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.sql.Timestamp
import java.text.DecimalFormat
import java.util.*

@Serializable
data class ConditionResponse(

        @Id
        @Serializable(with = UUIDSerializer::class)
        var conditions_id: UUID?,
        @Serializable(with = UUIDSerializer::class)
        var supplier_id: UUID?,
        @Serializable(with = BigDecimalSerializer::class)
        var price: BigDecimal,
        @Serializable(with = TimestampSerializer::class)
        var negotioation_timestamp: Timestamp?,
)

object UUIDSerializer : KSerializer<UUID> {
    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

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

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        return encoder.encodeString(value.toString())
    }
}

object TimestampSerializer : KSerializer<Timestamp> {
    override fun deserialize(decoder: Decoder): Timestamp {
        return Timestamp.valueOf(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        return encoder.encodeString(value.toString())
    }
}
