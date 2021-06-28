@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model.headquarter.order

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.util.*
import org.springframework.data.annotation.Id

@Serializable
data class OrderProduct(
    @Transient @Id var order_product_id: UUID? = null,
    @Transient var order_id: UUID? = null,
    var product_id: UUID,
    var count: Int
)
