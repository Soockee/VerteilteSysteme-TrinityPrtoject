package com.microservices.headquarterservice.tasks

import com.microservices.headquarterservice.service.ConditionService
import com.microservices.headquarterservice.service.TimeService
import org.slf4j.LoggerFactory
import org.springframework.cloud.context.restart.RestartEndpoint
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*


//@Component
//class RestartTask(
//    val timeService: TimeService
//) {
//    val logger = LoggerFactory.getLogger(RestartTask::class.java)
//    @Scheduled(fixedRate = 50)
//    fun restart() {
//        val restartEndpoint = RestartEndpoint()
//        val virtualNow = Date.from(timeService.getVirtualLocalTime(Instant.now().toEpochMilli()))
//        val virtualNowHours = virtualNow.hours
//        logger.warn("Current Time $virtualNowHours")
//        if (virtualNowHours == 0) {
//            val restartThread = Thread { restartEndpoint.restart() }
//            restartThread.isDaemon = false
//            restartThread.start()
//        }
//    }
//}