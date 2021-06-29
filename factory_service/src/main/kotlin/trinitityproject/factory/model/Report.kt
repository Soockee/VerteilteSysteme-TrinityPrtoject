package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import java.util.*

data class Report(
    @Transient @Id
    val reportId: UUID? = null,

    var ordersToday: Number,

    val producedGoods: Number,

    val productionCost: Number,

    val completedOrders: Number,

    val productivity: Number,

    val factoryName: String,

    val timestamp: Long
)