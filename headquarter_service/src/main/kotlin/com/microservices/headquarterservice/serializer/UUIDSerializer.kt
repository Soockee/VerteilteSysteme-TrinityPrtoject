package com.microservices.headquarterservice.serializer

import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.springframework.data.annotation.*

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
