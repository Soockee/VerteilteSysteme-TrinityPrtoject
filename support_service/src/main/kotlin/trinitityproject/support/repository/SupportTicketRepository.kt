package trinitityproject.support.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import trinitityproject.support.model.ticket.SupportTicket
import java.util.*

interface SupportTicketRepository : ReactiveMongoRepository<SupportTicket, UUID>