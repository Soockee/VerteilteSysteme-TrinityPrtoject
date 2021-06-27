@file:UseSerializers(UUIDSerializer::class, InstantSerializer::class)

package com.microservices.headquarterservice.model.supplier

import com.microservices.headquarterservice.serializer.InstantSerializer
import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant
import java.util.*

@Serializable
class SupplierOrderPartRequest(
    var part_id: UUID,
    var count: Int,
    var negotiationTimestamp: Instant?
)
