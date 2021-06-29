package trinityproject.support.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SupportTicketClosedException : ResponseStatusException {
    constructor() : super(HttpStatus.I_AM_A_TEAPOT)
    constructor(reason: String?) : super(HttpStatus.I_AM_A_TEAPOT, reason)
}