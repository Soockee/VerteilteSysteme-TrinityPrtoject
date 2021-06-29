package trinityproject.support.model.support

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.*

data class SupportTicketResponse(
    @Id
    @JsonProperty("supportTicketId")
    val supportTicketId: UUID?,
    @JsonProperty("customerId")
    val customerId: UUID,
    @JsonProperty("status")
    var status: Status,
    @JsonProperty("createTime")
    val createTime: Instant,
    @JsonProperty("supportTicketText")
    val supportTicketText: MutableList<SupportTicketText> = mutableListOf()
)
