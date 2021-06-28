// @file:UseSerializers(UUIDSerializer::class)
package com.microservices.headquarterservice.model.headquarter.condition

import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable(with = ConditionResponseSerializer::class)
class ConditionResponse(var part_id: UUID, var conditions: MutableList<Condition>)

@OptIn(
        kotlinx.serialization.InternalSerializationApi::class,
        kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializer(forClass = ConditionResponse::class)
object ConditionResponseSerializer : KSerializer<ConditionResponse> {
    override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("ConditionResponse") {
                element<String>("part_id")
                element<MutableList<Condition>>("conditions")
            }

    override fun serialize(encoder: Encoder, value: ConditionResponse) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.part_id.toString())
            encodeSerializableElement(
                    descriptor, 1, ListSerializer(Condition::class.serializer()), value.conditions)
        }
    }
    override fun deserialize(decoder: Decoder): ConditionResponse {
        return decoder.decodeStructure(descriptor) {
            var part_id: UUID? = null
            var conditions: List<Condition>? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop
                    0 -> part_id = UUID.fromString(decodeStringElement(descriptor, 0))
                    1 ->
                            conditions =
                                    decodeSerializableElement(
                                            descriptor,
                                            1,
                                            ListSerializer(Condition::class.serializer()))
                    else -> throw SerializationException("Unexpected index $index")
                }
            }

            ConditionResponse(
                    requireNotNull(part_id),
                    requireNotNull(conditions?.toMutableList()),
            )
        }
    }
}
