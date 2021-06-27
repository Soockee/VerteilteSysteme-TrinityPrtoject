package trinitityproject.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SupportTicketIdNotFoundException : ResponseStatusException {
    constructor() : super(HttpStatus.INTERNAL_SERVER_ERROR)
    constructor(reason: String?) : super(HttpStatus.INTERNAL_SERVER_ERROR, reason)
}