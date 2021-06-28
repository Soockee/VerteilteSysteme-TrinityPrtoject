package trinitityproject.factory.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class Product(

    @JsonProperty("product")
    val productData: ProductData,

    @JsonProperty("count")
    val count: Int,

    @JsonProperty("parts")
    val parts: List<Part>,

    @JsonProperty("status", access = JsonProperty.Access.READ_ONLY)
    var status: Status = Status.OPEN,

    @JsonProperty("status", access = JsonProperty.Access.READ_ONLY)
    var completionTime: Long
)
