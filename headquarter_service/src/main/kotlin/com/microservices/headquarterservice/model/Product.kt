@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*
import org.springframework.data.annotation.Id

@Serializable
data class Product(
        @Id var product_id: UUID?,
        var name: String,
        var production_time: Int,
)
