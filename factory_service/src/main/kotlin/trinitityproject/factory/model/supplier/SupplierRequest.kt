package trinitityproject.factory.model.supplier

import com.fasterxml.jackson.annotation.JsonProperty

data class SupplierRequest(
    @JsonProperty("supplierOrders")
    val supplierOrders: List<SupplierOrders>
)
