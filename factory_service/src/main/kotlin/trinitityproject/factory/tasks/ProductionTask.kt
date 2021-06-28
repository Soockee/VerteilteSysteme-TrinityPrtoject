package trinitityproject.factory.tasks

import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.service.ConditionService
import trinitityproject.factory.service.PartOrderService
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val conditionService: ConditionService
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @Scheduled(fixedRate = 4000)
    fun scheduleTaskWithFixedRate() {
        runBlocking {
            val productOrder = partOrderService.getUnfinishedProductOrder()
            val res = partOrderService
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
                .onEach {
                    partOrderService.addPartOrder(productOrder.productOrderId, it)
                }

            log.info(res.toString())
        }
    }

}


