package trinityproject.support.model.support

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty

enum class Status {
    @JsonEnumDefaultValue
    @JsonProperty("open")
    OPEN,
    @JsonProperty("closed")
    CLOSED
}
