package com.microservices.headquarterservice.persistence.support

import com.microservices.headquarterservice.model.support.SupportTicketText
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.*

interface SupportTicketTextRepository : ReactiveCrudRepository<SupportTicketText, UUID>