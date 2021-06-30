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
     * Sets the Product with the given ID to the given status.
     * A random product is picked since the index of a value could change.
     *
     * @return The updated ProductOrder
     */
    suspend fun setProductStatus(productOrderId: UUID, productId: UUID, status: Status): ProductOrder {
        val updatedProductOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .map { productOrder ->
                if(productOrder.partOrders.isEmpty()) productOrder

                val productIdx = productOrder
                    .products
                    .indexOfFirst {
                        (it.productData.productId == productId) && (it.status != status)
                    }

                productOrder.products[productIdx].status = status
                productOrder
            }
            .first()
        return repository
            .save(updatedProductOrder)
            .asFlow()
            .filterNotNull()
            .first()
    }
}