package trinitityproject.factory.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class Part(
    @Id
    @JsonProperty("part_id")
    val partId: UUID,

    @JsonProperty("count")
    val count: Int
)
