package com.microservices.headquarterservice.serializer

import java.sql.Timestamp
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.springframework.data.annotation.*
import java.time.Instant
/**
 * Instant Serializer is used for classes which contain fields of type Instant
 */
object InstantSerializer : KSerializer<Instant> {
    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        return encoder.encodeString(value.toString())
    }
}
