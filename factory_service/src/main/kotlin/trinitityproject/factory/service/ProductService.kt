package trinitityproject.factory.service

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

class ProductService(
    private val repository: ProductOrderRepository
) {
//    suspend fun updateProductOderStatus(productOrderId: UUID, productId: UUID, status: Status): ProductOrder {
//        val productOrder = repository
//            .findById(productOrderId)
//            .asFlow()
//            .filterNotNull()
//            .map { productOrder ->
//                val productIdx = productOrder
//                    .partOrders
//                    .indexOfFirst { it.partOrderId.to == "" }
//                productOrder.partOrders[productIdx].status = status
//                productOrder
//            }
//            .first()
//        return repository
//            .save(productOrder)
//            .asFlow()
//            .filterNotNull()
//            .first()
//    }
}