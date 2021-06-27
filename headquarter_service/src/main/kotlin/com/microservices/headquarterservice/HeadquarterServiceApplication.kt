package com.microservices.headquarterservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableR2dbcRepositories
@ConfigurationPropertiesScan("com.microservices.headquarterservice.config")
class HeadquarterServiceApplication{
    companion object {
        val logger = LoggerFactory.getLogger(HeadquarterServiceApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<HeadquarterServiceApplication>(*args)
}

// class Receiver {
//     fun receiveMessage(message: String) {
//       System.out.println("Received <" + message + ">");
//     }
//   }
