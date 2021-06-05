package trinitityproject.factory.model

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty

enum class Status {
    @JsonEnumDefaultValue
    @JsonProperty("open")
    OPEN,
    @JsonProperty("done")
    DONE
}
