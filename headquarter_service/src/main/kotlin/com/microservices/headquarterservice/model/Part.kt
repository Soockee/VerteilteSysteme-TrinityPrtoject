package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.*
import javax.annotation.processing.Generated

data class Part(
    @Id @Generated var part_id: UUID,
    var name: String,
)
