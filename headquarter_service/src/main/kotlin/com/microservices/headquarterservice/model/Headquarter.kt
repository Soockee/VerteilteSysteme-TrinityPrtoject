package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.UUID
import java.sql.Timestamp
import java.sql.Date

data class Headquarter(
    @Id var id: UUID?,
    var name: String,
    var description: String?
)

