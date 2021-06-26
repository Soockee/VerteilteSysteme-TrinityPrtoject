package trinitityproject.factory.model.condition

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ConditionRequest(
    @JsonProperty("partId", access = JsonProperty.Access.READ_ONLY)
    val partId: UUID,
)
