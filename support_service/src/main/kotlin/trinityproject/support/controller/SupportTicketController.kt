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
import java.util.*

@RestController("SupportTicketController")
class SupportTicketController(
    private val supportTicketService: SupportTicketService
) {
    private val logger: Logger = LoggerFactory.getLogger(SupportTicketService::class.java)

    @PatchMapping("/ticket")
    fun addTicketText(@RequestBody supportTicketTextRequest: SupportTicketTextRequest): Mono<SupportTicketResponse> {
        return runBlocking {
            logger.info("post request \"/ticket/\"")
            return@runBlocking supportTicketService.addTicketText(supportTicketTextRequest)
        }
    }

    @GetMapping("/ticket")
    fun getAll(@RequestParam status: String?): Flux<SupportTicket> {
        logger.info("get request \"/tickets/\"")
        return if(status == null) supportTicketService.getAll()
            else supportTicketService.getAllWithStatus(status)
    }

    @GetMapping("/ticket/{id}")
    fun getTicket(@PathVariable id: UUID): Mono<SupportTicketResponse> {
        return runBlocking {
            logger.info("get request \"/tickets/{id}\"")
            return@runBlocking supportTicketService.getTicketById(id)
        }
    }
}