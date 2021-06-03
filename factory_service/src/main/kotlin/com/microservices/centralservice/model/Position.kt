package com.microservices.centralservice.model

data class Position(
        val partSupplierId: UUID,
        val count: Number,
        val conditionId: UUID
)