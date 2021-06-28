package trinityproject.support.model.support

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class SupportTicketTextRequest(
    @Id
    @JsonProperty("supportTicketId")
    val supportTicketId: UUID,

    @JsonProperty("status")
    var status: Status,

    @JsonProperty("text")
    val text: String
)
