package trinitityproject.factory.tasks

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.Report
import trinitityproject.factory.service.ReportService
import trinitityproject.factory.service.TimeService
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableScheduling
class ReportTask(
    private val reportService: ReportService,
    private val timeService: TimeService,
    private val template: RabbitTemplate,
) : SchedulingConfigurer {
    private val logger: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @Bean
    fun taskExecutor(): Executor {
        return Executors.newSingleThreadScheduledExecutor()
    }

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor())

        taskRegistrar.addFixedRateTask({
            sendReport()
        }, timeService.getReportDelay())
    }

    /**
     * Sends a KPI message to the headquarter service
     */
    fun sendReport() {
        val report: Report = reportService.generateReport()

        if(report != null) {
            logger.info("sending kpi message to headquarter")

            val reportMessage = template.convertSendAndReceiveAsType(
                "kpi",
                report,
                object : ParameterizedTypeReference<Report>() {}
            )
        } else {
            logger.warn("kpi report could not be generated")
        }
    }
}
