package trinitityproject.factory.service

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
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


//    /**
//     * Creates the partOrders for a given productOrder
//     *
//     * @param productOrderId Id of the ProductOrder for which the PartOrders are to be created
//     */
//    fun createPartOrders(productOrder: ProductOrder) {
//        val neededParts = getRequiredParts(productOrder)
//
//        // TODO(Fabian): Submit PartOrders to corresponding Supplier
//    }

    // TODO(Fabian): PartOrder Updates fertigstellen
    /**
     * Updates the status of a partOrder
     *
     * @param productOrderId Id of the ProductOrder which contains the partOrder to be updated
     * @param id Id of the PartOrder to be updated
     * @param status status to be set
     */
    suspend fun updatePartOderStatus(productOrderId: UUID, partOrderId: UUID, status: Status) {
        val productOrder = repository.findById(productOrderId).asFlow().filterNotNull().first();
        val partOrder = productOrder
            .partOrders.first { it.partOrderId == partOrderId }
        partOrder.status = status
        productOrder.partOrders
        repository.save(productOrder)
    }

    /**
     * Angeben, welche Parts bei welchem Lieferanten geordert werden sollen?
     *
     *
     * @param productOrderId Id of the product order for which parts are to be ordered
     * @param supplierId Id of the supplier with whom the order is to be placed
     * @param positions List of the positions to be ordered
     */
    suspend fun addPartOrder(productOrderId: UUID, partOrder: PartOrder): ProductOrder {
        val productOrder = repository
            .findById(productOrderId)
            .asFlow()
            .filterNotNull()
            .toList()
            .first();

        productOrder.partOrders = productOrder.partOrders.plus(listOf(partOrder));

        return repository
            .save(productOrder)
            .asFlow()
            .filterNotNull()
            .first();
    }

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