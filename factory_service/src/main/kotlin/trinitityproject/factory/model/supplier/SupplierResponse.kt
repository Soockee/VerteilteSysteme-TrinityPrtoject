package trinitityproject.factory.model.supplier

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SupplierResponse(
    @JsonProperty("supplier_order")
    val supplierId: UUID,
)
