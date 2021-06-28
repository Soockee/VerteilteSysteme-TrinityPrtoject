package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.support.SupportTicket
import com.microservices.headquarterservice.model.support.SupportTicketRequest
import com.microservices.headquarterservice.model.support.SupportTicketResponse
import com.microservices.headquarterservice.model.support.SupportTicketText
import com.microservices.headquarterservice.persistence.support.SupportTicketRepository
import com.microservices.headquarterservice.persistence.support.SupportTicketTextRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class SupportTicketService(
    private val supportTicketRepository: SupportTicketRepository,
    private val supportTicketTextRepository: SupportTicketTextRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    /**
     * Creates a new ticket and saves it in the database.
     *
     * @param supportTicketRequest A new support ticket.
     * @return Returns the created ticket.
     */
    fun createTicket(supportTicketRequest: SupportTicketRequest): Mono<SupportTicketResponse> {
        val supportTicket: SupportTicket =
            supportTicketRepository.save(SupportTicket(customerId = supportTicketRequest.customerId)).block()!!

        val supportTicketTextCreated: SupportTicketText = supportTicketTextRepository.save(
            SupportTicketText(
                supportTicketId = supportTicket.supportTicketId,
                text = supportTicketRequest.text,
                changeTime = supportTicket.createTime
            )
        ).block()!!

        val supportTicketResponse = SupportTicketResponse(
            supportTicketId = supportTicket.supportTicketId,
            customerId = supportTicket.customerId,
            status = supportTicket.status,
            createTime = supportTicket.createTime
        )
        supportTicketResponse.supportTicketText.add(supportTicketTextCreated)

        logger.info("Support ticket response was created: $supportTicketResponse")

        return Mono.just(supportTicketResponse)
    }
}