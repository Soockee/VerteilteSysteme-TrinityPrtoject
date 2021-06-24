package trinitityproject.factory.service

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import trinitityproject.factory.model.ProductOrder
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
    suspend fun createPartOrders(productOrderId: UUID) {
        val productOrderFlow = repository.findById(productOrderId).asFlow();

        if (productOrderFlow.count() < 1) {
            return;
        }

        var productOrder = productOrderFlow.toList().first();
        val products = productOrder.products.toMutableList();

        var neededParts : MutableMap<UUID, Number> = HashMap()

        products.forEach { product ->
            product.parts.forEach { part ->
                if(neededParts.containsKey(part.partId)) {
                    neededParts.set(part.partId, neededParts.getValue(part.partId).toInt() + part.count.toInt());
                } else {
                    neededParts.set(part.partId, part.count);
                }
            };
        };

        productOrder.partOrders = this.toPartOrders(neededParts);

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
            if(partOrdersMap.containsKey(supplierId)) {
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

    fun getRequiredParts(order: ProductOrder): List<Pair<UUID, Int>> {
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
            .aggregate { key, acc: Pair<UUID, Int>?, element, first ->
                if (first)
                    Pair(key, element.second)
                else
                    Pair(key, acc!!.second + element.second)
            }
            .map { it.value }
    }

}