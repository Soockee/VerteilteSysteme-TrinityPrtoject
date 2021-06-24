@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
class OrderProductRequest(
    var product_id: UUID,
    var count: Int,
)
