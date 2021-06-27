
package com.microservices.headquarterservice.model.headquarter

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable(with = ProductResponseSerializer::class)
data class ProductResponse(
    var product: Product,
    // var parts: Map<UUID, Int>, // part_id, count
    var parts: List<ProductPart>
)


@OptIn(
    kotlinx.serialization.InternalSerializationApi::class,
    kotlinx.serialization.ExperimentalSerializationApi::class
)
@Serializer(forClass = ProductResponse::class)
object ProductResponseSerializer : KSerializer<ProductResponse> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ProductResponse") {
            element<Product>("product")
            element<List<ProductPart>>("parts")
        }

    override fun serialize(encoder: Encoder, value: ProductResponse) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, Product::class.serializer(), value.product)
            encodeSerializableElement(
                descriptor, 1, ListSerializer(ProductPart::class.serializer()), value.parts
            )
        }
    }

    override fun deserialize(decoder: Decoder): ProductResponse {
        return decoder.decodeStructure(descriptor) {
            var product: Product? = null
            var parts: List<ProductPart>? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop
                    0 -> product = decodeSerializableElement(descriptor, 0, Product::class.serializer())
                    1 -> parts =
                        decodeSerializableElement(
                            descriptor,
                            1,
                            ListSerializer(ProductPart::class.serializer())
                        )
                    else -> throw SerializationException("Unexpected index $index")
                }
            }

            ProductResponse(
                requireNotNull(product),
                requireNotNull(parts),
            )
        }
    }
}