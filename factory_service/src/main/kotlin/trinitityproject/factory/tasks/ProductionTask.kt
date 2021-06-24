package trinitityproject.factory.tasks

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler

@Component
class ProductionTask {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @Scheduled(fixedRate = 1000)
    fun scheduleTaskWithFixedRate() {
        log.error("Here is the Production")
        Thread.sleep(500)
    }

}