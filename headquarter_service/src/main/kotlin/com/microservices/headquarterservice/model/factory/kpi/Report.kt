package com.microservices.headquarterservice.model.factory.kpi

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.util.*

data class Report(
    @kotlin.jvm.Transient @Id
    val reportId: UUID? = null,

    var ordersToday: Number,

    val producedGoods: Number,

    val productionCost: Number,

    val openOrders: Number,

    val productivity: Number,

    val factoryName: String,

    val createdAt: Long
)