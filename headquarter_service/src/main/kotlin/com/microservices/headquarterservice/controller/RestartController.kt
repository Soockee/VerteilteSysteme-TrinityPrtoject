package com.microservices.headquarterservice.controller

import com.microservices.headquarterservice.service.RestartService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("RestartController")
class RestartController(
    val restartService: RestartService
) {
    @GetMapping("/restart")
    fun restart() {
        return restartService.restartApp()
    }
}
