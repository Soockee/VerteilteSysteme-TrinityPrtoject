package com.microservices.centralservice.model

import org.springframework.data.annotation.Id
import java.util.UUID

data class Central(
    @Id var id: UUID?,
    var name: String,
    var description: String?
)
