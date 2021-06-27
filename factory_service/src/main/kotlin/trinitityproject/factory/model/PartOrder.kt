package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import java.util.*

data class PartOrder(
    @Id val partOrderId: UUID,
    var status: Status = Status.OPEN,
    val supplierId: UUID,
    val orderId: UUID?,
    val positions: List<Position>
)
