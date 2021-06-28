@file:UseSerializers(UUIDSerializer::class)
package com.microservices.headquarterservice.model.headquarter

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.springframework.data.annotation.Id
import java.util.*

@Serializable
class OrderResponse (
    @Id var customer_id: UUID,
    var products: List<OrderProduct>
)