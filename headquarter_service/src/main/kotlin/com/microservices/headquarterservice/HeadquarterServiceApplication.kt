package com.microservices.headquarterservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@SpringBootApplication
@EnableR2dbcRepositories
@EnableScheduling
@ConfigurationPropertiesScan
class HeadquarterServiceApplication{
    companion object {
        val logger = LoggerFactory.getLogger(HeadquarterServiceApplication::class.java)
    }
}


fun main(args: Array<String>) {
    runApplication<HeadquarterServiceApplication>(*args)
}
