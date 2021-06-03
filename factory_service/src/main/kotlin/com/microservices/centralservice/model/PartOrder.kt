package com.microservices.centralservice.model

data class PartOrder(
        @Id val partOrderId: UUID,
        var status: Status,
        val supplierId: UUID,
        val positions: List<Position>
)