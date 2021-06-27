@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model.supplier

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
class SupplierOrderRequest (
        var productOrders: List<SupplierOrderPartRequest>
)