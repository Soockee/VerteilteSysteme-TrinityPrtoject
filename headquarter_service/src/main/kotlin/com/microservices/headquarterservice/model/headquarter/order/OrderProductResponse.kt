@file:UseSerializers(UUIDSerializer::class)
package com.microservices.headquarterservice.model.headquarter.order

import com.microservices.headquarterservice.model.headquarter.Product
import com.microservices.headquarterservice.model.headquarter.ProductPart
import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class OrderProductResponse (
    var product: Product,
    var count: Int,
    var parts: List<ProductPart>
)