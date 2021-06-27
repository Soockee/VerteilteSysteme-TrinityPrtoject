@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model.supplier

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
class SupplierOrderPartRequest(
    var order_id: UUID,
)
