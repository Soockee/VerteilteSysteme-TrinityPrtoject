package trinitityproject.support.model.ticket

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import trinitityproject.support.model.Status
import java.util.*

@Document
data class SupportTicketTextRequest (
    @Id
    @JsonProperty("supportTicketId")
    val supportTicketId: UUID,

    @JsonProperty("status")
    var status: Status,

    @JsonProperty("text")
    val text: String
)
