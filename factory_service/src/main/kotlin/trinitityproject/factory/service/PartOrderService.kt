package trinitityproject.factory.service

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service
import trinitityproject.factory.model.*
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

@Service
class PartOrderService(
    private val repository: ProductOrderRepository
) {


    /**
     * Updates the status of a partOrder
     *
     * @param productOrderId Id of the ProductOrder which contains the partOrder to be updated
     * @param partOrderId Id of the PartOrder to be updated
     * @param status status to be set
     */
    suspend fun updatePartOderStatus(productOrderId: UUID, partOrderId: UUID, status: Status): ProductOrder {
        val productOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .map { productOrder: ProductOrder ->
                val productIdx = productOrder
                    .partOrders
                    .indexOfFirst { it.partOrderId == partOrderId }
                productOrder.partOrders[productIdx].status = status
                productOrder
            }
            .first()
        return repository
            .save(productOrder)
            .asFlow()
            .filterNotNull()
            .first()
    }

    /**
     * A List of part-order will be added to a ProductOrder
     *
     * @param productOrderId Id of the product order for which parts are to be ordered
     * @param partOrders The part-order which will be added
     *
     * @return The updated ProductOrder
     */
    suspend fun addPartOrders(productOrderId: UUID, partOrders: List<PartOrder>): ProductOrder {
        val productOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .toList()
            .first()

        productOrder.partOrders = partOrders

        return repository
            .save(productOrder)
            .asFlow()
            .filterNotNull()
            .first()
    }

    /**
     * Returns a product from the database which is not finished
     */
    suspend fun getUnfinishedProductOrder(): ProductOrder {
        return repository
            .findAll(
                Example.of(
                    ProductOrder(
                        customerId = UUID.fromString("cf9ae254-c466-11eb-8529-0242ac130003"),
                        status = Status.OPEN,
                        products = listOf()
                    ),
                    ExampleMatcher
                        .matchingAny()
                        .withIgnorePaths(
                            "productOrderId",
                            "customerId",
                            "receptionTime",
                            "products",
                            "partOrders"
                        )
                )
            )
            .asFlow()
            .first()
    }


    /**
     * Aggregates the required parts over all products in a ProductOrder
     */
    suspend fun getRequiredParts(order: ProductOrder): Map<UUID, Int> {
        return order.products
            .map { product ->
                product
                    .parts
                    .map { part ->
                        Pair(part.partId, (part.count * product.count))
                    }
            }
            .flatten()
            .groupingBy {
                it.first
            }
            .aggregate { _, acc: Int?, element, first ->
                if (first)
                    element.second
                else
                    acc!! + element.second
            }
    }

}