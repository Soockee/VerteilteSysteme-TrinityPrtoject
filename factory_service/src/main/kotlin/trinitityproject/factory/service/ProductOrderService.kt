package trinitityproject.factory.service

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

@Component
class ProductOrderService(
    private val repository: ProductOrderRepository,
    @Value("\${factory.name}") val factoryName: String,
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderService::class.java)

    fun createOrder(order: ProductOrder) {
        val test = ProductOrder(order.productOrderId,
            order.customerId,
            order.receptionTime,
            order.status,
            order.products,
            order.partOrders,
            factoryName)
        val submitedOrder = repository.insert(test).block()
        log.info("Saved to order to database: $submitedOrder")
    }

    /**
     * Get an order for a specific ID
     * @param uuid ProductOrder Id
     */
    suspend fun getOrder(uuid: UUID): ProductOrder {
        return repository
            .findById(uuid)
            .asFlow()
            .filterNotNull()
            .first()
    }

    /**
     * Updates the status of a ProductOrder
     * @param status The state which will be set
     *
     * @return The updated ProductOrder
     */
    suspend fun updateProductOrderState(productOrderId: UUID, status: Status): ProductOrder {
        val productOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .map { productOrder ->
                productOrder.status = status
                productOrder
            }
            .first()
        return repository
            .save(productOrder)
            .asFlow()
            .filterNotNull()
            .first()
    }
}