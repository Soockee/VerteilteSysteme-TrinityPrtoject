package trinitityproject.support.service

import ch.qos.logback.core.status.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import trinitityproject.support.model.ticket.SupportTicket
import trinitityproject.support.model.ticket.SupportTicketText
import trinitityproject.support.model.ticket.SupportTicketTextRequest
import trinitityproject.support.repository.SupportTicketRepository

@Component
class SupportTicketService(
    private val repository: SupportTicketRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    /**
     * Add a ticket text and set the status. This is only possible if the ticket is currently being processed or it is already closed.
     *
     * @param supportTicketTextRequest A request object to add a text and the status to a ticket.
     * @return The support ticket that was edited.
     */
    fun addTicketText(supportTicketTextRequest: SupportTicketTextRequest) : Mono<SupportTicket> {
        logger.info("Add ticket text and set status: $supportTicketTextRequest")

        var supportTicket: SupportTicket =  repository.findById(supportTicketTextRequest.supportTicketId).block()!!

        if (supportTicket.status == trinitityproject.support.model.Status.CLOSED || supportTicket.inProgress.isNotEmpty())
            return Mono.empty()

        supportTicket.inProgress = "IND"
        repository.save(supportTicket)

        supportTicket.supportTicketText.add(SupportTicketText(supportTicketTextRequest.text))
        supportTicket.status = supportTicketTextRequest.status
        supportTicket.inProgress = "";

        return repository.save(supportTicket)
    }

    /**
     * Get alls support tickets with $status from database.
     *
     * @param status The status of the tickets.
     * @return A list of all support tickets with $status.
     */
    fun getAllWithStatus(status: String): Flux<SupportTicket> {
        if (status.lowercase() == "inprogress")
            return repository.findAll().filter { ticket -> ticket.inProgress.isNotEmpty() }
        else
            return repository.findAll().filter { ticket -> ticket.status.toString().lowercase() == status.lowercase() }
    }

    /**
     * Get all support tickets from database.
     *
     * @return A list of all support tickets.
     */
    fun getAll(): Flux<SupportTicket> {
        return repository.findAll()
    }

    /**
     * Check if the support ticket Id exists.
     *
     * @return true if the support ticket Id exist.
     */
    fun existsSupportTicketId(supportTicketTextRequest: SupportTicketTextRequest): Boolean {
        return repository.existsById(supportTicketTextRequest.supportTicketId).block()!!
    }
}