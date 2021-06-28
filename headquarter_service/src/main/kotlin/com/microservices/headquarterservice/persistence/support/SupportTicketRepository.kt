package com.microservices.headquarterservice.persistence.support

import com.microservices.headquarterservice.model.support.SupportTicket
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.*

interface SupportTicketRepository : ReactiveCrudRepository<SupportTicket, UUID>