package com.microservices.headquarterservice.model.support

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class SupportTicketRequest(
    @Id
    @JsonProperty("customerId")
    val customerId: UUID,

    @JsonProperty("text")
    val text: String
)
