package trinitityproject.factory.tasks

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.condition.Condition
import trinitityproject.factory.model.condition.ConditionRequest
import trinitityproject.factory.service.PartOrderService
import java.util.*


@Component
class ProductionTask(
    private val partOrderService: PartOrderService,
    private val template: RabbitTemplate,
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)


    @Scheduled(fixedRate = 4000)
    fun scheduleTaskWithFixedRate() {
        val productOrder = partOrderService.getUnfinishedProductOrder()
        if (productOrder != null) {
            val conditionId = UUID.randomUUID()
            log.info("send request for$conditionId")

            val requiredParts = partOrderService.getRequiredParts(productOrder)
            val sk = template.convertSendAndReceiveAsType(
                "conditionRequests",
                ConditionRequest(conditionId),
                object : ParameterizedTypeReference<Condition>() {}
            )
            log.info(sk.toString())
        } else {
            log.warn("No suitable product order found")
        }
    }

}
