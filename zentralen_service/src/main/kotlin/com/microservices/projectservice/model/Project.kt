package com.microservices.projectservice.model

import org.springframework.data.annotation.Id
import java.util.UUID

data class Project(
    @Id var id: UUID?,
    var name: String,
    var description: String?
)
