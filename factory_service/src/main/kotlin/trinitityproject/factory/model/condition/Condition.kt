package trinitityproject.factory.model.condition

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Condition(
    @JsonProperty("conditionsId")
    val conditionsId: UUID,

    @JsonProperty("conditionsId")
    val supplierId: UUID,

    @JsonProperty("value")
    val price: Number, //better approach would be  price: {Number, currency: enum{euro}}

    @JsonProperty("partSupplierId")
    val partSupplierId: UUID, //foreign ID of the part at the supplier

    @JsonProperty("negotiationTimestamp")
    val negotiationTimestamp: Long
)
