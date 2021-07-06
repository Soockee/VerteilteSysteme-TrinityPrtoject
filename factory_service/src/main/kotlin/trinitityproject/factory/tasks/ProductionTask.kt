package trinitityproject.factory.tasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.model.supplier.*
import trinitityproject.factory.service.*
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val conditionService: ConditionService,
    private val productOrderService: ProductOrderService,
    private val productService: ProductService,
    private val jacksonWebClient: WebClient,
    private val timeService: TimeService,
    @Value("\${factory.timezone}") val timeZone: String
) {
    private val log: Logger = LoggerFactory.getLogger(ProductionTask::class.java)


    @Scheduled(fixedDelay = 500)
    fun scheduleTaskWithFixedRate() {
        val currDate = LocalDate.ofInstant(
            timeService.getVirtualLocalTime(System.currentTimeMillis()),
            TimeZone.getTimeZone(timeZone).toZoneId()
        )
        if ((currDate.dayOfWeek == DayOfWeek.SATURDAY) || (currDate.dayOfWeek == DayOfWeek.SUNDAY)) {
            log.info("today it's: ${currDate.dayOfWeek}, WE DON'T WORK\"")
        } else {
            log.info("today it's: ${currDate.dayOfWeek}, WE WORK")
            runBlocking {
                try {
                    var productOrder = partOrderService.getUnfinishedProductOrder()
                    log.warn("processing order ${productOrder.productOrderId}")
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
                                    .uri("/supplier/order")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(supplierRequest)
                                    .retrieve()
                                    .bodyToMono(SupplierResponse::class.java)
                                    //The creation of a part-order will be requested 3 time with an 2 second delay which will be doubled on each retry
                                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                                    .block()!!
                                partOrder.orderId = res.supplierId
                            }
                        //If all orders have been accepted by the suppliers, the orders will be saved to the database
                        productOrder = partOrderService.addPartOrders(
                            productOrderId = productOrder.productOrderId,
                            partOrders = partOrders
                        )
                        productOrder = productOrderService.getOrder(productOrder.productOrderId)
                        log.warn("added part orders to order: ${productOrder.productOrderId}")
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
                                            .path("/supplier/order/")
                                            .path("/{id}")
                                            .build(it.orderId!!.toString())
                                    }
                                    .retrieve()
                                    .bodyToMono(SupplierStatusResponse::class.java)
                                    //The status of an order will be requested 3 time with an 2 second delay which will be doubled on each retry
                                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                                    .block()!!

                                log.warn("got response for part order: $res")
                                if (res.status == SupplierStatus.COMPLETE) {
                                    productOrder = partOrderService.updatePartOderStatus(
                                        productOrderId = productOrder.productOrderId,
                                        partOrderId = it.partOrderId,
                                        status = Status.DONE
                                    )
                                    log.warn("updated part Order status for order ${productOrder.productOrderId}")
                                }
                            }
                        }

                    val noProductOrdersArePending = productOrder
                        .partOrders
                        .none { it.status == Status.OPEN }

                    val productsAreOpen = productOrder
                        .products.any { it.status == Status.OPEN }


                    // If one part-order is not completed productOrder will be discarded and picked up later
                    if (noProductOrdersArePending && productsAreOpen) {
                        for (product in productOrder.products) {
                            // Wait for each product the given production-time
                            val startTime = System.currentTimeMillis()
                            log.info("started production of ${product.count} - ${product.productData.name} with production-time ${product.productData.productionTime}: $startTime")
                            runBlocking {
                                delay(timeService.realtimeToVirtualTimeMillis(product.productData.productionTime) * product.count)
                            }
                            log.info("finished production of ${product.count} - ${product.productData.name} after: ${System.currentTimeMillis() - startTime}")

                            productOrder = productService.setProductStatusToDone(
                                productOrderId = productOrder.productOrderId,
                                productId = product.productData.productId
                            )
                        }

                        // If all products are produced the product order will be set to done
                        productOrder = productOrderService
                            .updateProductOrderState(
                                productOrderId = productOrder.productOrderId,
                                status = Status.DONE
                            )
                        log.info("finished products for order ${productOrder.productOrderId}")
                    } else {
                        log.info("part orders still pending for order ${productOrder.productOrderId}")
                    }
                } catch (e: NoSuchElementException) {
                    log.warn("no open orders")
                } catch (e: Exception) {
                    // Show
                    log.error("production got interrupted: " + e.printStackTrace())
                }
            }
        }
    }

}


