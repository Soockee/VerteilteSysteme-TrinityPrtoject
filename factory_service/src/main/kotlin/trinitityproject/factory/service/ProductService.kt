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
     * Sets the Product with the given ID to done.
     * A random product is picked since the index of a value could change.
     *
     * @return The updated ProductOrder
     */
    suspend fun setProductStatusToDone(productOrderId: UUID, productId: UUID): ProductOrder {
        val updatedProductOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .map { productOrder ->

                val productIdx = productOrder
                    .products
                    .indexOfFirst {
                        ((it.productData.productId == productId) && (it.status == Status.OPEN))
                    }

                productOrder.products[productIdx].status = Status.DONE
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