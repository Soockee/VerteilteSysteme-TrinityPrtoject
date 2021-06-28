package trinityproject.support.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import trinityproject.support.model.support.SupportTicket
import java.util.*

interface SupportTicketRepository : ReactiveCrudRepository<SupportTicket, UUID>