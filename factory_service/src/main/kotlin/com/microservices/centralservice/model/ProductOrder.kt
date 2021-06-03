package com.microservices.centralservice.model

import org.springframework.data.annotation.Id
import java.util.*

data class ProductOrder(
    @Id val productOrderId: UUID,
    val customerId: UUID,
    val receptionTime: Long = System.currentTimeMillis(),
    var status: Status = Status.OPEN,
    var products: List<Product> = listOf(),
    var partOrders: List<PartOrder> = listOf()
)