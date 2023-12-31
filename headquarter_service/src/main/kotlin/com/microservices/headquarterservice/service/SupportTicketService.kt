package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.supplier.SupplierOrderResponse
import com.microservices.headquarterservice.model.support.SupportTicket
import com.microservices.headquarterservice.model.support.SupportTicketRequest
import com.microservices.headquarterservice.model.support.SupportTicketResponse
import com.microservices.headquarterservice.model.support.SupportTicketText
import com.microservices.headquarterservice.persistence.support.SupportTicketRepository
import com.microservices.headquarterservice.persistence.support.SupportTicketTextRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class SupportTicketService(
    private val supportTicketRepository: SupportTicketRepository,
    private val supportTicketTextRepository: SupportTicketTextRepository,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.queueSupport}") val headquarterSupportQueue: String,
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    /**
     * Creates a new ticket and saves it in the database.
     *
     * @param supportTicketRequest A new support ticket.
     * @return Returns the created ticket.
     */
    fun createTicket(supportTicketRequest: SupportTicketRequest): Mono<SupportTicketResponse> {
        val supportTicket: SupportTicket = SupportTicket(
            UUID.randomUUID(),
            customerId = supportTicketRequest.customerId
        )


        val supportTicketTextCreated: SupportTicketText =
            SupportTicketText(
                UUID.randomUUID(),
                supportTicketId = supportTicket.supportTicketId,
                text = supportTicketRequest.text,
                changeTime = supportTicket.createTime
            )


        val supportTicketResponse = SupportTicketResponse(
            supportTicketId = supportTicket.supportTicketId,
            customerId = supportTicket.customerId,
            status = supportTicket.status,
            createTime = supportTicket.createTime
        )
        supportTicketResponse.supportTicketText.add(supportTicketTextCreated)

        logger.info("Support ticket response was created: $supportTicketResponse")
        send(supportTicketResponse)
        return Mono.just(supportTicketResponse)
    }

    fun send(ticket: SupportTicketResponse) {
        rabbitTemplate.convertAndSend(
            headquarterSupportQueue,
            Json.encodeToString(ticket) as Any
        ) { message ->
            message.messageProperties.contentType = "application/json"
            message.messageProperties.deliveryMode = MessageDeliveryMode.PERSISTENT
            message
        }
        logger.info("Send msg = " + ticket)
    }
}