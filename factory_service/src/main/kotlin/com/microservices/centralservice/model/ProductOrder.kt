package com.microservices.centralservice.model

import org.springframework.data.annotation.Id
import java.util.*

data class ProductOrder(
    @Id val productOrderId: UUID,
    val customerId: UUID,
    val receptionTime: Long,
    var status: Status,
    var products: List<Product>,
    var partOders: List<PartOrder>
)