package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import java.util.*

data class Report(
    @Transient @Id
    val reportId: UUID? = null,
    var orderCount: Number,
    val finishedGoodsCount: Number,
    val costs: Number,
    val factoryName: String
)