package trinityproject.support.model.support

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant
import java.util.*

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