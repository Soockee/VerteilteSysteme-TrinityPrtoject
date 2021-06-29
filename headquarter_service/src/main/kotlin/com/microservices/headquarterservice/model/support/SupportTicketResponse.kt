@file:UseSerializers(UUIDSerializer::class, InstantSerializer::class)

package com.microservices.headquarterservice.model.support

import com.microservices.headquarterservice.serializer.BigDecimalSerializer
import com.microservices.headquarterservice.serializer.InstantSerializer
import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.*

@Serializable
data class SupportTicketResponse(
    @Id
    val supportTicketId: UUID?,
    val customerId: UUID,
    var status: Status,
    val createTime: Instant,
    val supportTicketText: MutableList<SupportTicketText> = mutableListOf()
)