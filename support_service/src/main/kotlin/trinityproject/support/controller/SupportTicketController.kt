package trinityproject.support.controller

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import trinityproject.support.model.support.SupportTicket
import trinityproject.support.model.support.SupportTicketResponse
import trinityproject.support.model.support.SupportTicketTextRequest
import trinityproject.support.service.SupportTicketService

@RestController("SupportTicketController")
class SupportTicketController(
    private val supportTicketService: SupportTicketService
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    @PostMapping("/ticket/")
    fun addTicketText(@RequestBody supportTicketTextRequest: SupportTicketTextRequest): Mono<SupportTicketResponse> {
        return runBlocking {
            logger.info("post request \"/ticket/\"")
            return@runBlocking supportTicketService.addTicketText(supportTicketTextRequest)
        }
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