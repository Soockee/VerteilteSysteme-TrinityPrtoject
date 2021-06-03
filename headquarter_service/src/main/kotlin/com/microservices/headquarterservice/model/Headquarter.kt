package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.UUID

data class Headquarter(
    @Id var id: UUID?,
    var name: String,
    var description: String?
)
