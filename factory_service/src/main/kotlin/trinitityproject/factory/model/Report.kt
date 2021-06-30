package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import java.util.*

data class Report(
    @Transient @Id
    val reportId: UUID? = null,

    var ordersToday: Number,

    val producedGoods: Number,

    val productionCost: Number,

    val openOrders: Number,

    val productivity: Number,

    val factoryName: String,

    val createdAt: Long
)