package com.microservices.headquarterservice.model

import java.util.UUID
import org.springframework.data.annotation.Id

data class Headquarter(@Id var id: UUID?, var name: String, var description: String?)
