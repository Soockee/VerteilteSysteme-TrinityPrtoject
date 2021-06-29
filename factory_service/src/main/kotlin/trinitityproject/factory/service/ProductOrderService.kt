package trinitityproject.factory.service

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

@Component
class ProductOrderService(
    private val repository: ProductOrderRepository
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderService::class.java)

    fun createOrder(order: ProductOrder) {
        val submitedOrder = repository.insert(order).block()
        log.info("Saved to order to database: $submitedOrder")
    }

    suspend fun getOrder(uuid: UUID): ProductOrder {
        return repository
            .findById(uuid)
            .asFlow()
            .filterNotNull()
            .first()
    }

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