package trinitityproject.factory.tasks

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.PartOrder
import trinitityproject.factory.model.Position
import trinitityproject.factory.model.Status
import trinitityproject.factory.model.condition.Condition
import trinitityproject.factory.model.condition.ConditionRequest
import trinitityproject.factory.model.condition.PartCondition
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
        val productOrder = partOrderService.getUnfinishedProductOrder()
        if (productOrder != null) {

            val outcome = partOrderService
                .getRequiredParts(productOrder)
                .map { Pair(conditionService.getBestCondition(it.key), it.value) }
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
                                it.first.conditions_id
                            )
                        }
                    )
                }.onEach {
                    partOrderService.addPartOrder(productOrder.productOrderId, it)
                }
        }

//        if (productOrder != null) {
//            val conditionId = UUID.randomUUID()
//            log.info("send request for$conditionId")
//
//
//            val sk = template.convertSendAndReceiveAsType(R
//                "conditionRequests",
//                ConditionRequest(conditionId),
//                object : ParameterizedTypeReference<Condition>() {}
//            )
//            log.info(sk.toString())
//        } else {
//            log.warn("No suitable product order found")
//        }
    }

}
