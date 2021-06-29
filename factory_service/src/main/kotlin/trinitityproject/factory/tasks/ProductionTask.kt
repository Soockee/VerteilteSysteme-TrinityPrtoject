package trinitityproject.factory.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ExchangeStrategies
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
import java.lang.IllegalStateException
import java.time.Duration
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val conditionService: ConditionService,
    private val productOrderService: ProductOrderService,
    private val productService: ProductService
) {
    private val log: Logger = LoggerFactory.getLogger(ProductionTask::class.java)

    private val jacksonStrategies = ExchangeStrategies
        .builder()
        .codecs { clientDefaultCodecsConfigurer: ClientCodecConfigurer ->
            clientDefaultCodecsConfigurer.defaultCodecs()
                .jackson2JsonEncoder(Jackson2JsonEncoder(ObjectMapper(), MediaType.APPLICATION_JSON))
            clientDefaultCodecsConfigurer.defaultCodecs()
                .jackson2JsonDecoder(Jackson2JsonDecoder(ObjectMapper(), MediaType.APPLICATION_JSON))
        }.build()

    private val client = WebClient
        .builder()
        .baseUrl("http://localhost:8080")
        .exchangeStrategies(jacksonStrategies)
        .build()

    @Scheduled(fixedRate = 10000)
    fun scheduleTaskWithFixedRate() {
        runBlocking {
            try {
                var productOrder = partOrderService.getUnfinishedProductOrder()
                log.warn("Process: $productOrder")
                if (productOrder.partOrders.isEmpty()) {
                    partOrderService
                        .getRequiredParts(productOrder)
                        .map {
                            Pair(conditionService.getBestCondition(it.key), it.value)
                        }
                        .groupBy { it.first.supplier_id } //All
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
                            val supplierRequest = SupplierRequest(supplierOrders = partOrder.positions
                                .map {
                                    SupplierOrders(
                                        part_id = it.partSupplierId,
                                        count = it.count
                                    )
                                }
                            )
                            log.warn(supplierRequest.toString())
                            val res = client
                                .post()
                                .uri("/supplier")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(supplierRequest)
                                .retrieve()
                                .bodyToMono(SupplierResponse::class.java)
                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                                .block()!!
                            partOrder.orderId = res.supplierId
                            log.warn("orderID: " + res.supplierId.toString())
                            partOrderService.addPartOrder(productOrder.productOrderId, partOrder)
                        }
                    productOrder = productOrderService.getOrder(productOrder.productOrderId)
                    log.warn("Added part Orders to productOrder: $productOrder")
                }

                productOrder.partOrders
                    .filter { it.status == Status.OPEN }
                    .onEach {
                        if (it.orderId == null) {
                            throw IllegalStateException("orderId must be set for ")
                        }
                        if (it.orderId != null) {
                            val res = client
                                .get()
                                .uri { uriBuilder ->
                                    uriBuilder
                                        .path("/supplier-order/")
                                        .queryParam("order_id", it.orderId!!.toString())
                                        .build()
                                }
                                .retrieve()
                                .bodyToMono(SupplierStatusResponse::class.java)
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
                if (noProductOrdersArePending) {
                    for (product in productOrder.products) {
                        val startTime = System.currentTimeMillis()
                        log.info("Started Production of ${product.productData.name}: $startTime")
                        delay(product.completionTime)
                        log.info("Finished Production of ${product.productData.name} after: ${System.currentTimeMillis() - startTime}")
                        productService.setProductStatus(
                            productOrder.productOrderId,
                            product.productData.productId,
                            Status.DONE
                        )
                    }
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
                log.error("Production got interrupted: " + e.printStackTrace())
            }
        }
    }

}


