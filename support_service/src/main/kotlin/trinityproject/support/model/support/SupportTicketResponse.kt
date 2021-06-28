package trinityproject.support.model.support

import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.*

data class SupportTicketResponse(
    @Id
    val supportTicketId: UUID?,
    val customerId: UUID,
    var status: Status,
    val createTime: Instant,
    val supportTicketText: MutableList<SupportTicketText> = mutableListOf()
)
