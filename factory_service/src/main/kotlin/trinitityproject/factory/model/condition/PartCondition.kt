package trinitityproject.factory.model.condition

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PartCondition(
    @JsonProperty("part_id")
    val partId: UUID,

    @JsonProperty("conditions")
    val conditions: List<Condition>
)
