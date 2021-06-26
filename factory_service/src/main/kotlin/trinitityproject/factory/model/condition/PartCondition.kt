package trinitityproject.factory.model.condition

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PartCondition(
    @JsonProperty("partId", access = JsonProperty.Access.READ_ONLY)
    val partId: UUID,

    @JsonProperty("conditions", access = JsonProperty.Access.READ_ONLY)
    val conditions: List<Condition>
)
