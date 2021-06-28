package trinitityproject.factory.model.supplier

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SupplierOrders(
    @JsonProperty("part_id")
    val part_id: UUID,

    @JsonProperty("count")
    val count: Int
)
