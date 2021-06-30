package com.microservices.headquarterservice.service

import org.springframework.cloud.context.restart.RestartEndpoint
import org.springframework.stereotype.Service

@Service
class RestartService(
    val restartEndpoint: RestartEndpoint
) {
    fun restartApp() {
        restartEndpoint.restart()
    }
}