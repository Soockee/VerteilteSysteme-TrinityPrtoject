@file:UseSerializers(UUIDSerializer::class)

package com.microservices.headquarterservice.model.supplier

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.util.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Serializable
@Table("supplier_order_part")
data class  SupplierOrderPart(
        @Transient  @Id var supplier_order_part_id: UUID? = null,
        @Transient var supplier_order_id: UUID? = null,
        @Transient var part_id: UUID? = null,
        var count: Int,
)
