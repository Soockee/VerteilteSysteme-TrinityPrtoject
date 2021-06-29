package trinityproject.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SupportTicketIdNotFoundException : ResponseStatusException {
    constructor() : super(HttpStatus.NOT_FOUND)
    constructor(reason: String?) : super(HttpStatus.NOT_FOUND, reason)
}