package com.microservices.headquarterservice.serializer

import java.sql.Timestamp
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.springframework.data.annotation.*

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
