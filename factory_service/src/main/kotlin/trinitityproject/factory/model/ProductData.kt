package trinitityproject.factory.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class ProductData(
    @Id
    @JsonProperty("product_id")
    val productId: UUID = UUID.randomUUID(),

    @JsonProperty("production_time")
    val productionTime: Long,

    @JsonProperty("name", required = false)
    val name: String
)
