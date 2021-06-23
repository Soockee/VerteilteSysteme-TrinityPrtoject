package com.microservices.headquarterservice.serializer

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.springframework.data.annotation.*

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
