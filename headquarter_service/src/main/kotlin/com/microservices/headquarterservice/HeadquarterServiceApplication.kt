package com.microservices.headquarterservice

import com.microservices.headquarterservice.config.HeadquarterServiceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories
@ConfigurationPropertiesScan("com.microservices.headquarterservice.config")
class HeadquarterServiceApplication

fun main(args: Array<String>) {
    runApplication<HeadquarterServiceApplication>(*args)
}
