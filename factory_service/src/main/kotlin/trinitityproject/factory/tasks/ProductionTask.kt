package trinitityproject.factory.tasks

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.model.supplier.*
import trinitityproject.factory.service.ConditionService
import trinitityproject.factory.service.PartOrderService
import trinitityproject.factory.service.ProductOrderService
import trinitityproject.factory.service.ProductService
import java.time.Duration
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val conditionService: ConditionService,
    private val productOrderService: ProductOrderService,
    private val productService: ProductService,
    private val jacksonWebClient: WebClient
) {
    private val log: Logger = LoggerFactory.getLogger(ProductionTask::class.java)


    @Scheduled(fixedRate = 3000)
    fun scheduleTaskWithFixedRate() {
        runBlocking {
            try {
                var productOrder = partOrderService.getUnfinishedProductOrder()
                log.warn("Process: $productOrder")
                if (productOrder.partOrders.isEmpty()) {
                    val partOrders = partOrderService
                        .getRequiredParts(productOrder)
                        .map {
                            Pair(
                                conditionService.getBestCondition(it.key),
                                it.value
                            )// The counts of the parts are aggregated
                        }
                        .groupBy { it.first.supplier_id } //The Conditions get Grouped by their supplier
                        .map { supplierConditionMap ->
                            PartOrder(
                                UUID.randomUUID(),
                                Status.OPEN,
                                supplierConditionMap.key,
                                null,
                                supplierConditionMap.value.map {
                                    Position(
                                        it.first.part_id,
                                        it.second,
                                        it.first
                                    )
                                }
                            )
                        }
                        .onEach { partOrder ->
                            // The request to create the part-orders are send to the supplier endpoint
                            val supplierRequest = SupplierRequest(supplierOrders = partOrder.positions
                                .map {
                                    SupplierOrders(
                                        part_id = it.partSupplierId,
                                        count = it.count
                                    )
                                }
                            )
                            log.warn(supplierRequest.toString())
                            //If one order has not been accepted the current productOrder will be discarded and picked up later
                            val res = jacksonWebClient
                                .post()
                                .uri("/supplier")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(supplierRequest)
                                .retrieve()
                                .bodyToMono(SupplierResponse::class.java)
                                //The creation of a part-order will be requested 3 time with an 2 second delay which will be doubled on each retry
                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                                .block()!!
                            log.warn("orderID: " + res.supplierId.toString())
                            partOrder.orderId = res.supplierId
                        }
                    //If all orders have been accepted by the suppliers, the orders will be saved to the database
                    productOrder = partOrderService.addPartOrders(
                        productOrderId = productOrder.productOrderId,
                        partOrders = partOrders
                    )
                    productOrder = productOrderService.getOrder(productOrder.productOrderId)
                    log.warn("Added part Orders to productOrder: $productOrder")
                }

                // It will be checked that all part-orders are completed by the supplier
                productOrder.partOrders
                    .filter { it.status == Status.OPEN }
                    .onEach {
                        if (it.orderId == null) {
                            throw IllegalStateException("orderId must be set for ")
                        }
                        if (it.orderId != null) {
                            val res = jacksonWebClient
                                .get()
                                .uri { uriBuilder ->
                                    uriBuilder
                                        .path("/supplier-order/")
                                        .queryParam("order_id", it.orderId!!.toString())
                                        .build()
                                }
                                .retrieve()
                                .bodyToMono(SupplierStatusResponse::class.java)
                                //The status of an order will be requested 3 time with an 2 second delay which will be doubled on each retry
                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                                .block()!!

                            log.warn("Got Response: $res")
                            if (res.status == SupplierStatus.COMPLETE) {
                                productOrder = partOrderService.updatePartOderStatus(
                                    productOrderId = productOrder.productOrderId,
                                    partOrderId = it.partOrderId,
                                    status = Status.DONE
                                )
                                log.warn("Updated Part Order Status: $productOrder")
                            }
                        }
                    }

                val noProductOrdersArePending = productOrder
                    .partOrders
                    .none { it.status == Status.OPEN }

                // If one part-order is not completed productOrder will be discarded and picked up later
                if (noProductOrdersArePending) {
                    for (product in productOrder.products) {
                        // Wait for each product the given production-time
                        val startTime = System.currentTimeMillis()
                        log.info("")
                        log.info("Started Production of ${product.productData.name} with the production-time ${product.productData.productionTime}: $startTime")
                        Thread.sleep(product.productData.productionTime)
                        log.info("Finished Production of ${product.productData.name} after: ${System.currentTimeMillis() - startTime}")
                        productService.setProductStatus(
                            productOrder.productOrderId,
                            product.productData.productId,
                            Status.DONE
                        )
                    }
                    // If all products are produced the product order will be set to done
                    productOrder = productOrderService
                        .updateProductOrderState(
                            productOrderId = productOrder.productOrderId,
                            status = Status.DONE
                        )
                    log.info("Finished Product: $productOrder")
                } else {
                    log.info("Part Orders are still pending: $productOrder")
                }
            } catch (e: Exception) {
                // Show
                log.error("Production got interrupted: " + e.printStackTrace())
            }
        }
    }

}


