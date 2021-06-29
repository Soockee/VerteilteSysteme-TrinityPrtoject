package trinityproject.support.model.support

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class SupportTicketTextRequest(
    @JsonProperty("supportTicketId")
    val supportTicketId: UUID,

    @JsonProperty("status")
    var status: Status,

    @JsonProperty("text")
    val text: String
)
