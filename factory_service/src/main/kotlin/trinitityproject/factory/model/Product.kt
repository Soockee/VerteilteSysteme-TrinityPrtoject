package trinitityproject.factory.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class Product(
    @Id
    @JsonProperty("productId")
    val productId: UUID = UUID.randomUUID(),

    @JsonProperty("count")
    val count: Number,

    @JsonProperty("status", access = JsonProperty.Access.READ_ONLY)
    var status: Status = Status.OPEN,

    @JsonProperty("productionTime")
    val productionTime: Long,

    @JsonProperty("parts")
    val parts: List<Part>
)
