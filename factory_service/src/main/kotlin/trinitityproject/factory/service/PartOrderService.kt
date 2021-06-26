package trinitityproject.factory.service

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.*
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

@Service
class PartOrderService(
    private val repository: ProductOrderRepository
) {


    /**
     * Creates the partOrders for a given productOrder
     *
     * @param productOrderId Id of the ProductOrder for which the PartOrders are to be created
     */
    fun createPartOrders(productOrder: ProductOrder) {
        val neededParts = getRequiredParts(productOrder)

        // TODO(Fabian): Submit PartOrders to corresponding Supplier
    }

    // TODO(Fabian): PartOrder Updates fertigstellen
    /**
     * Updates the status of a partOrder
     *
     * @param productOrderId Id of the ProductOrder which contains the partOrder to be updated
     * @param id Id of the PartOrder to be updated
     * @param status status to be set
     */
    suspend fun updatePartOderStatus(productOrderId: UUID, partOrderId: UUID, status: Status) {
        val productOrderFlow = repository.findById(productOrderId).asFlow();

        if (productOrderFlow.count() < 1) {
            return;
        }

        var productOrder = productOrderFlow.toList().first();
    }

    /**
     * Angeben, welche Parts bei welchem Lieferanten geordert werden sollen?
     *
     *
     * @param productOrderId Id of the product order for which parts are to be ordered
     * @param supplierId Id of the supplier with whom the order is to be placed
     * @param positions List of the positions to be ordered
     */
    suspend fun addPartOrder(productOrderId: UUID, supplierId: UUID, positions: List<Position>) {
        val partOrder = PartOrder(UUID.randomUUID(), Status.OPEN, supplierId, positions);

        val productOrderFlow = repository.findById(productOrderId).asFlow();

        if (productOrderFlow.count() < 1) {
            return;
        }

        var productOrder = productOrderFlow.toList().first();

        productOrder.partOrders = productOrder.partOrders.plus(listOf(partOrder));

        repository.save(productOrder);
    }

    // TODO(Fabian): get ID and Count of every Part und check, where to order it
    fun toPartOrders(neededParts: MutableMap<UUID, Number>): List<PartOrder> {
        var partOrdersMap: MutableMap<UUID, PartOrder> = HashMap();
        var partOrders: MutableList<PartOrder> = ArrayList();

        neededParts.keys.forEach { partId ->
            val supplierId: UUID = this.getSuitableSupplier(partId);
            if (partOrdersMap.containsKey(supplierId)) {
                var partOrder: PartOrder = partOrdersMap.get(supplierId)!!;
                //partOrder.positions
            } else {
                var positions: MutableList<Position> = ArrayList();

                // TODO(Fabian): Get partId from Supplier
                // TODO(Fabian): Get conditionId
                // positions.add(Position(partId, neededParts.get(partId))
                // var partOrder = PartOrder(UUID.randomUUID(), Status.OPEN, supplierId, );

            }
        }

        return partOrders;
    }

    // TODO(Fabian): Check, where to order the part
    fun getSuitableSupplier(partId: UUID): UUID {
        return UUID.randomUUID();
    }

    fun getUnfinishedProductOrder(): ProductOrder? {
        return repository.findAll(
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
        ).blockFirst()
    }

    fun getRequiredParts(order: ProductOrder): Map<UUID, Int> {
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