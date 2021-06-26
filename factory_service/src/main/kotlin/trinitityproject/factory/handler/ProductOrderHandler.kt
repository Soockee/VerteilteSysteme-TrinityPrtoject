package trinitityproject.factory.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import trinitityproject.factory.model.*
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*
import javax.annotation.PostConstruct


@Component
class ProductOrderHandler(
    private val repository: ProductOrderRepository,
    private val amqpAdmin: AmqpAdmin,
    private val amqpTemplate: AmqpTemplate
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @PostConstruct
    fun setupQueueDestinations() {
//        //Setup of the quueues defined in application.properties
//        destinationsConfig.getQueues()
//            .forEach { (key: String?, destination: DestinationsConfig.DestinationInfo) ->
//                log.info(
//                    "[I54] Creating directExchange: key={}, name={}, routingKey={}",
//                    key,
//                    destination.exchange,
//                    destination.routingKey
//                )
//                val ex: Exchange = ExchangeBuilder.directExchange(
//                    destination.exchange
//                )
//                    .durable(true)
//                    .build()
//                amqpAdmin.declareExchange(ex)
//                val q = QueueBuilder.durable(
//                    destination.routingKey
//                )
//                    .build()
//                amqpAdmin.declareQueue(q)
//                val b = BindingBuilder.bind(q)
//                    .to(ex)
//                    .with(destination.routingKey)
//                    .noargs()
//                amqpAdmin.declareBinding(b)
//            }
    }

    @Suppress("UNUSED_PARAMETER")
    suspend fun getProductOrderServices(request: ServerRequest): ServerResponse {
        val productOrders: Flow<ProductOrder> = repository.findAll().asFlow()
        return ok().contentType(APPLICATION_JSON).bodyAndAwait(productOrders)
    }

    suspend fun getProductOrderService(request: ServerRequest): ServerResponse {
        val productOrderId = UUID.fromString(request.pathVariable("id"))
        val productOrder = repository
            .findById(productOrderId)
            .asFlow()
        if (productOrder.count() < 1) {
            return ServerResponse
                .notFound()
                .buildAndAwait()
        }
        return ok()
            .contentType(APPLICATION_JSON)
            .bodyAndAwait(productOrder)
    }

    suspend fun createProductOrderService(request: ServerRequest): ServerResponse {
        val productOrder = request.awaitBody<ProductOrder>()
        repository
            .save(productOrder)
            .subscribe()
        return ok().buildAndAwait()
    }

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

        var neededParts: MutableMap<UUID, Number> = HashMap()

        products.forEach { product ->
            product.parts.forEach { part ->
                if (neededParts.containsKey(part.partId)) {
                    neededParts.set(part.partId, neededParts.getValue(part.partId).toInt() + part.count.toInt());
                } else {
                    neededParts.set(part.partId, part.count);
                }
            };
        };

        productOrder.partOrders = this.toPartOrders(neededParts);

        // TODO(Fabian): Submit PartOrders to corresponding Supplier
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
     * Updates the status of a product in a productOrder
     *
     * @param productOrderId Id of the ProductOrder which contains the product to be updated
     * @param productId Id of the Product to be updated
     * @param status status to be set
     */
    suspend fun updateProductStatus(productOrderId: UUID, productId: UUID, status: Status) {
        val productOrderFlow = repository.findById(productOrderId).asFlow();

        if (productOrderFlow.count() < 1) {
            return;
        }

        var productOrder = productOrderFlow.toList().first();

        var products = productOrder.products.toMutableList()

        val productIndex = products.indexOfFirst { product -> product.productId == productId };

        var product = productOrder.products.get(productIndex);

        product.status = status;

        products[productIndex] = product;

        productOrder.products = products;

        repository.save(productOrder);
    }

    /**
     * Updates the status of a productOrder
     *
     * @param productOrderId Id of the ProductOrder to be updated
     * @param status status to be set
     */
    suspend fun updateProductOrderStatus(productOrderId: UUID, status: Status) {
        val productOrderFlow = repository.findById(productOrderId).asFlow();

        if (productOrderFlow.count() < 1) {
            return;
        }

        var productOrder = productOrderFlow.toList().first();

        productOrder.status = status;

        repository.save(productOrder);
    }

//    - update partOrder list -> Es muss moeglich sein neue partOrders hinzuzufuegen
//    - update Status -> ProductOrder,PartOrder, Product
//    - evtl. delete routen
}