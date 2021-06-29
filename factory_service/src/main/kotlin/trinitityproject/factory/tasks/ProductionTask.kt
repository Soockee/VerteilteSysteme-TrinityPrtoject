package trinitityproject.factory.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
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
import org.springframework.web.reactive.function.client.bodyToFlow
import reactor.kotlin.core.publisher.toMono
import reactor.util.retry.Retry
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.model.condition.Condition
import trinitityproject.factory.model.supplier.*
import trinitityproject.factory.service.ConditionService
import trinitityproject.factory.service.PartOrderService
import trinitityproject.factory.service.ProductOrderService
import java.lang.IllegalStateException
import java.time.Duration
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val conditionService: ConditionService,
    private val productOrderService: ProductOrderService
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
                if (productOrder.partOrders.isEmpty()) {
                    val ll = partOrderService
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
                    log.info("Added part Orders to productOrder: $productOrder")
                }

                log.info("Found no entry with partOrders")

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
                                        .path("/products/{id}/attributes/{attributeId}")
                                        .build(it.orderId!!.toString())
                                }
                                .retrieve()
                                .bodyToMono(SupplierStatusResponse::class.java)
                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                                .block()!!
                            if (res.status == SupplierStatus.COMPLETE) {
                                it.
                            }
                        }
                    }
//                val orderHasPendingSupplierRequests = productOrder.partOrders.filter { it.status. }

            } catch (e: Exception) {
                log.error("Production got interrupted: " + e.printStackTrace())
            }
        }
    }

}


