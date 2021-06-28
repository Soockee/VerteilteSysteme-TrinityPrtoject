@file:UseSerializers(UUIDSerializer::class, InstantSerializer::class)

package com.microservices.headquarterservice.model.support

import com.microservices.headquarterservice.serializer.BigDecimalSerializer
import com.microservices.headquarterservice.serializer.InstantSerializer
import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant
import java.util.*

@Serializable
data class SupportTicketText(
        @Id
        @Column("support_ticket_text_id")
        val supportTicketTextId: UUID? = null,
        @Column("support_ticket_id")
        val supportTicketId: UUID?,
        @Column("text")
        val text: String,
        @Column("change_time")
        val changeTime: Instant = Instant.now()
)