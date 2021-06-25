package trinitityproject.factory.tasks

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.service.PartOrderService

@Component
class ProductionTask(
    private val partOrderService: PartOrderService
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @Scheduled(fixedRate = 4000)
    fun scheduleTaskWithFixedRate() {
        log.info(partOrderService.getUnfinishedProductOrder().block().toString())

    }

}