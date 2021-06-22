package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import java.util.*

data class Part(
    @Id val partId: UUID,
    val count: Number
)
