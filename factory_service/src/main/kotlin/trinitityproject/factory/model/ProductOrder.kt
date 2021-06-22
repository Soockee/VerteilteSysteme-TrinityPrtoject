package trinitityproject.factory.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class ProductOrder(
    @Id val productOrderId: UUID = UUID.randomUUID(),
    val customerId: UUID,
    val receptionTime: Long = System.currentTimeMillis(),
    var status: Status = Status.OPEN,
    var products: List<Product>,
    var partOrders: List<PartOrder>
)
