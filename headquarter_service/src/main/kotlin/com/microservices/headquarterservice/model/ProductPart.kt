@file:UseSerializers(UUIDSerializer::class)


package com.microservices.headquarterservice.model

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.springframework.data.annotation.Id
import java.util.*

@Serializable
class ProductPart(
    @Transient @Id var product_part_id: UUID? = null,
    @Transient var product_id: UUID? = null,
    var part_id: UUID,
    var count: Int,
)
