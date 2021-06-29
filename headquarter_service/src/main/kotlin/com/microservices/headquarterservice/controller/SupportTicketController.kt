package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.model.support.SupportTicketRequest
import com.microservices.headquarterservice.model.support.SupportTicketResponse
import com.microservices.headquarterservice.service.SupportTicketService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController("SupportTicketController")
class SupportTicketController(
    private val supportTicketService: SupportTicketService
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketController::class.java)

    /**
     * Calls the support ticket service so that a new ticket is written to the database.
     *
     * @param supportTicketRequest A new support ticket.
     * @return Returns the created ticket.
     */
    @PostMapping("/ticket")
    fun createTicket(@RequestBody supportTicketRequest: SupportTicketRequest): Mono<SupportTicketResponse> {
        logger.info("POST Request: \"/ticket\": $supportTicketRequest")
        return supportTicketService.createTicket(supportTicketRequest)
    }
}