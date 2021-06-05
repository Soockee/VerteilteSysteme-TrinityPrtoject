package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import java.util.*

data class Product(
    @Id val productId: UUID,
    val count: Number,
    var status: Status = Status.OPEN,
    val productionTime: Long,
    val parts: List<Part>
)
