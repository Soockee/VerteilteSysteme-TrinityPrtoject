package trinitityproject.support.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.extra.bool.not
import trinitityproject.support.model.ticket.SupportTicket
import trinitityproject.support.model.ticket.SupportTicketTextRequest
import trinitityproject.support.service.SupportTicketService

@RestController("SupportTicketController")
class SupportTicketController (
    private val supportTicketService: SupportTicketService
){
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    @PostMapping("/ticket/")
    fun addTicketText(@RequestBody supportTicketTextRequest: SupportTicketTextRequest): Mono<SupportTicket> {
        logger.info("post request \"/ticket/\"")

        if (!supportTicketService.existsSupportTicketId(supportTicketTextRequest))
            return Mono.empty()

        return supportTicketService.addTicketText(supportTicketTextRequest)
    }

    @GetMapping("/tickets/{status}")
    fun getAllWithStatus(@PathVariable status: String): Flux<SupportTicket> {
        logger.info("get request \"/tickets/{status}\"")
        return supportTicketService.getAllWithStatus(status)
    }

    @GetMapping("/tickets/")
    fun getAll(): Flux<SupportTicket> {
        logger.info("get request \"/tickets/\"")
        return supportTicketService.getAll()
    }
}