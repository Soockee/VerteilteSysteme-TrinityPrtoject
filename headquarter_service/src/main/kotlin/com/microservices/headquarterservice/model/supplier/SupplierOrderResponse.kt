@file:UseSerializers(UUIDSerializer::class)
package com.microservices.headquarterservice.model.supplier

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.springframework.data.annotation.Id
import java.util.*

@Serializable
class SupplierOrderResponse (
    var supplier_order: UUID,
)