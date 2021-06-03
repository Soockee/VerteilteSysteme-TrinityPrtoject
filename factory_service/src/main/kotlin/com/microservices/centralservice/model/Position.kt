package com.microservices.centralservice.model

import java.util.*

data class Position(
        val partSupplierId: UUID,
        val count: Number,
        val conditionId: UUID
)