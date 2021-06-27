package trinitityproject.support.model.ticket

import com.fasterxml.jackson.annotation.JsonProperty

data class SupportTicketText (
    @JsonProperty("text")
    val text: String,

    @JsonProperty("changeTime")
    val changeTime: Long = System.currentTimeMillis()
)