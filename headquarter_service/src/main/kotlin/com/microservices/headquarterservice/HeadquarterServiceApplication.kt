package com.microservices.headquarterservice

import com.microservices.headquarterservice.config.CentralServiceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories
@EnableConfigurationProperties(CentralServiceProperties::class)
class CentralServiceApplication

fun main(args: Array<String>) {
    runApplication<CentralServiceApplication>(*args)
}
