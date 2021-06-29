package trinityproject.support.model.support

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant
import java.util.*


data class SupportTicketText(
    @Id
    @Column("support_ticket_text_id")
    @JsonProperty("supportTicketTextId")
    val supportTicketTextId: UUID? = null,

    @Column("support_ticket_id")
    @JsonProperty("supportTicketId")
    val supportTicketId: UUID?,

    @Column("text")
    @JsonProperty("supportTicketId")
    val text: String,

    @Column("change_time")
    @JsonProperty("supportTicketId")
    val changeTime: Instant = Instant.now()
)