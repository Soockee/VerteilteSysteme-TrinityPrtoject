package trinityproject.support.service

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import trinityproject.support.exception.SupportTicketClosedException
import trinityproject.support.exception.SupportTicketIdNotFoundException
import trinityproject.support.model.support.*
import trinityproject.support.repository.SupportTicketRepository
import trinityproject.support.repository.SupportTicketTextRepository
import java.time.Instant
import java.util.*
import kotlin.NoSuchElementException

@Component
class SupportTicketService(
    private val supportTicketRepository: SupportTicketRepository,
    private val supportTicketTextRepository: SupportTicketTextRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    /**
     * Add a ticket text and set the status. This is only possible if the ticket is currently being processed
     * or it is already closed.
     *
     * @param supportTicketTextRequest A request object to add a text and the status to a ticket.
     * @return The support ticket that was edited.
     */
    suspend fun addTicketText(supportTicketTextRequest: SupportTicketTextRequest): Mono<SupportTicketResponse> {
        logger.info("Starting to add ticket text")

        var supportTicketMono: Mono<SupportTicket> = supportTicketRepository
            .findById(
                supportTicketTextRequest
                    .supportTicketId
            )
        try {
            var supportTicket: SupportTicket = supportTicketMono.asFlow()
                .filterNotNull()
                .first()
            if (supportTicket.status == Status.CLOSED) {
                logger.info("The ticket is already closed!")
                return Mono.error(SupportTicketClosedException("ticket is closed"))
            }

            if (supportTicket.status != supportTicketTextRequest.status) {
                supportTicket.status = supportTicketTextRequest.status

                supportTicket = supportTicketRepository.save(supportTicket).asFlow().filterNotNull().first()
            }

            supportTicketTextRepository.save(
                SupportTicketText(
                    supportTicketId = supportTicket.supportTicketId,
                    text = supportTicketTextRequest.text,
                    changeTime = Instant.now()
                )
            ).asFlow().filterNotNull().first()

            val supportTicketResponse = SupportTicketResponse(
                supportTicketId = supportTicket.supportTicketId,
                customerId = supportTicket.customerId,
                status = supportTicket.status,
                createTime = supportTicket.createTime,
                mutableListOf()
            )

            supportTicketTextRepository.findAll().collectList().asFlow().filterNotNull().first()
                .filter { e -> e.supportTicketId == supportTicket.supportTicketId }.forEach { e ->
                    if (e.supportTicketId == supportTicketResponse.supportTicketId)
                        supportTicketResponse.supportTicketText.add(e)
                }

            logger.info("The edited ticket: $supportTicketResponse")
            return Mono.just(supportTicketResponse)
        } catch (exception: NoSuchElementException) {
            logger.info("Ticket not found!")
            return Mono.error(SupportTicketIdNotFoundException("Ticket Not Found"))
        }
    }

    /**
     * Creates a new ticket and saves it to the database.
     *
     * @param supportTicketResponse The ticket from the queue.
     */
    fun createTicket(supportTicketResponse: SupportTicketResponse) {
        val supportTicket: SupportTicket = supportTicketRepository.save(
            SupportTicket(
                customerId = supportTicketResponse.customerId,
                status = supportTicketResponse.status,
                createTime = supportTicketResponse.createTime
            )
        ).block()!!

        supportTicketResponse.supportTicketText.forEach { ticket ->
            supportTicketTextRepository.save(
                SupportTicketText(
                    supportTicketId = supportTicket.supportTicketId,
                    text = ticket.text,
                    changeTime = Instant.now()
                )
            )
        }

        logger.info("Ticket created: $supportTicket")
    }

    /**
     * Get all support tickets with $status from database.
     *
     * @param status The status of the tickets.
     * @return A list of all support tickets with $status.
     */
    fun getAllWithStatus(status: String): Flux<SupportTicket> {
        return supportTicketRepository.findAll()
            .filter { ticket -> ticket.status.toString().lowercase() == status.lowercase() }
    }

    /**
     * Get all support tickets from database.
     *
     * @return A list of all support tickets.
     */
    fun getAll(): Flux<SupportTicket> {
        return supportTicketRepository.findAll()
    }

    /**
     * Get all support tickets from database.
     *
     * @return A list of all support tickets.
     */
    suspend fun getTicketById(id: UUID): Mono<SupportTicketResponse> {
        try {
            val supportTicket = supportTicketRepository.findById(id).asFlow()
                .filterNotNull()
                .first()

            var supportTicketResponse = SupportTicketResponse(
                supportTicket.supportTicketId,
                supportTicket.customerId,
                supportTicket.status,
                supportTicket.createTime,
                mutableListOf()
            )

            supportTicketTextRepository.findAll().collectList().asFlow().filterNotNull().first()
                .filter { e -> e.supportTicketId == supportTicket.supportTicketId }.forEach { e ->
                    if (e.supportTicketId == supportTicketResponse.supportTicketId)
                        supportTicketResponse.supportTicketText.add(e)
                }

            return Mono.just(supportTicketResponse)
        } catch (exception: NoSuchElementException) {
            logger.info("Ticket not found!")
            return Mono.error(SupportTicketIdNotFoundException("Ticket Not Found"))
        }
    }
}