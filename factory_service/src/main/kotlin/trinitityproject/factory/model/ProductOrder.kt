package trinitityproject.factory.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class ProductOrder(
    @Id
    @JsonProperty("productOrderId", access = JsonProperty.Access.READ_ONLY)
    val productOrderId: UUID = UUID.randomUUID(),

    @JsonProperty("customer_id")
    val customerId: UUID,

    @JsonProperty("receptionTime", access = JsonProperty.Access.READ_ONLY)
    val receptionTime: Long = System.currentTimeMillis(),

    @JsonProperty("status", access = JsonProperty.Access.READ_ONLY)
    var status: Status = Status.OPEN,

    @JsonProperty("products")
    var products: List<Product>,

    @JsonProperty("partOrders", access = JsonProperty.Access.READ_ONLY)
    var partOrders: List<PartOrder> = listOf(),

    @JsonProperty("factoryName", access = JsonProperty.Access.READ_ONLY)
    val factoryName: String = ""
)

