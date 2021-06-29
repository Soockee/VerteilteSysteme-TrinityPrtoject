package com.microservices.headquarterservice.model.factory.kpi

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.util.*

data class Report(
    @Transient @Id
    val reportId: UUID? = null,
    var orderCount: Number,
    val finishedGoodsCount: Number,
    val costs: Number,
    val factoryName: String
)