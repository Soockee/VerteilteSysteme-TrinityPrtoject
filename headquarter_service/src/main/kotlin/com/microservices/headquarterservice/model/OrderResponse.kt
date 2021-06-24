package com.microservices.headquarterservice.model

import org.springframework.data.annotation.Id
import java.util.*

class OrderResponse (
    @Id var customer_id: UUID,
    var products: List<OrderProduct>
)