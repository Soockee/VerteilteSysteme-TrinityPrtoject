package com.microservices.headquarterservice.tasks

import com.microservices.headquarterservice.HeadquarterServiceApplication
import com.microservices.headquarterservice.service.TimeService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*


@Component
class RestartTask(
    val timeService: TimeService,
    var context: ConfigurableApplicationContext
) {
    val logger = LoggerFactory.getLogger(RestartTask::class.java)
    var doneOnce = false

    @Scheduled(fixedRate = 500)
    fun restart() {
        val virtualNow = Date.from(timeService.getVirtualLocalTime(Instant.now().toEpochMilli()))
        val virtualNowHours = virtualNow.hours
        logger.warn("Current Time $virtualNowHours in Hours")
        if (virtualNowHours == 0 && !doneOnce) {
            val args: ApplicationArguments = context.getBean(ApplicationArguments::class.java)
            val restartThread = Thread {
                logger.warn("Restarting Application!")
                context.close();
                context = runApplication<HeadquarterServiceApplication>(*args.sourceArgs)
            }
            restartThread.isDaemon = false
            restartThread.start()
            doneOnce = true
        } else if (virtualNowHours > 2 && doneOnce) {
            doneOnce = false
        }
    }
}