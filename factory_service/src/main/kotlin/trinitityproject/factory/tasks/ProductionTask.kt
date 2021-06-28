package trinitityproject.factory.tasks

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toMono
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.service.ConditionService
import trinitityproject.factory.service.PartOrderService
import trinitityproject.factory.service.ProductOrderService
import java.lang.Exception
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val conditionService: ConditionService,
    private val productOrderService: ProductOrderService
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @Scheduled(fixedRate = 4000)
    fun scheduleTaskWithFixedRate() {
        runBlocking {
            try {
                var productOrder = partOrderService.getUnfinishedProductOrder()
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
                        .forEach {
                            partOrderService.addPartOrder(productOrder.productOrderId, it)
                        }

                    productOrder = productOrderService.getOrder(productOrder.productOrderId)
                }

                val orderHasPendingSupplierRequests = productOrder.partOrders.filter { it.status. }

                if () {
                }
            } catch (e: Exception) {
                log.error("Production got interrupted: " + e.stackTrace.toString())
            }
        }
    }

}


