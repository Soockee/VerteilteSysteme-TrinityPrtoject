package trinityproject.support.model.support

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant
import java.util.*

data class SupportTicket(
    @Id
    @Column("support_ticket_id")
    val supportTicketId: UUID? = null,
    @Column("customer_id")
    val customerId: UUID,
    @Column("status")
    var status: Status = Status.OPEN,
    @Column("create_time")
    val createTime: Instant = Instant.now()
)