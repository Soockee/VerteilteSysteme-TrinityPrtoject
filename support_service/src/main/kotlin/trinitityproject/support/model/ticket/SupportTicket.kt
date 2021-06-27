package trinitityproject.support.model.ticket

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import trinitityproject.support.model.Status
import java.util.*

@Document
data class SupportTicket (
    @Id
    @JsonProperty("supportTicketId")
    val supportTicketId: UUID,

    @JsonProperty("customerId")
    val customerId: UUID,

    @JsonProperty("status")
    var status: Status,

    @JsonProperty("createTime")
    val createTime: Long,

    @JsonProperty("inProgress")
    var inProgress: String,

    @JsonProperty("supportTicketText")
    val supportTicketText: MutableList<SupportTicketText>
)