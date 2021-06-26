package trinitityproject.factory.model.condition

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Condition(
    @JsonProperty("conditions_id")
    val conditions_id: UUID,

    @JsonProperty("supplier_id")
    val supplier_id: UUID,

    @JsonProperty("part_id")
    val part_id: UUID, //foreign ID of the part at the supplier

    @JsonProperty("price")
    val price: Number, //better approach would be  price: {Number, currency: enum{euro}}

    @JsonProperty("currency")
    val currency: String,

    @JsonProperty("negotiationTimestamp")
    val negotiationTimestamp: Long
)
