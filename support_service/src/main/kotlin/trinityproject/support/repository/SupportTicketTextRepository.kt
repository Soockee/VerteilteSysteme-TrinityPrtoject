package trinityproject.support.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import trinityproject.support.model.support.SupportTicketText
import java.util.*

interface SupportTicketTextRepository : ReactiveCrudRepository<SupportTicketText, UUID>