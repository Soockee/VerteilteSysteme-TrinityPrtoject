package trinitityproject.factory.model.supplier

import com.fasterxml.jackson.annotation.JsonProperty

data class SupplierStatusResponse(
    @JsonProperty("status")
    val status: SupplierStatus
)
