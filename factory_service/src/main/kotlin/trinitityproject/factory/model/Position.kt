package trinitityproject.factory.model

import trinitityproject.factory.model.condition.Condition
import java.util.*

data class Position(
    val partSupplierId: UUID,
    val count: Number,
    val condition: Condition
)
