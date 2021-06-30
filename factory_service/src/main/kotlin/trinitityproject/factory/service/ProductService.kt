package trinitityproject.factory.service

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Component
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

@Component
class ProductService(
    private val repository: ProductOrderRepository
) {

    /**
     * Sets all Products of the given productOrderId to done.
     *
     * @return The updated ProductOrder
     */
    suspend fun setAllProductStatusToDone(productOrderId: UUID): ProductOrder {
        val updatedProductOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .first()

        updatedProductOrder.products.forEach {
            it.status = Status.DONE
        }

        return repository
            .save(updatedProductOrder)
            .asFlow()
            .filterNotNull()
            .first()
    }
}