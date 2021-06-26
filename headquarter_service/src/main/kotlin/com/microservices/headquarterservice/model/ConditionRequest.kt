@file:UseSerializers(UUIDSerializer::class)


package com.microservices.headquarterservice.model

import com.microservices.headquarterservice.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
data class ConditionRequest(
    val partId: UUID,
)

