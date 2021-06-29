package trinitityproject.factory.model.supplier

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty

enum class SupplierStatus {

    @JsonEnumDefaultValue
    @JsonProperty("complete")
    COMPLETE,


    @Suppress("unused")
    @JsonProperty("uncomplete")
    UNCOMPLETE


}