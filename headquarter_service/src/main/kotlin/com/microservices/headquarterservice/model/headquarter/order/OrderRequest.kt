@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model.headquarter

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
class OrderRequest (
        var customer_id: UUID,
        var products: List<OrderProductRequest>
)