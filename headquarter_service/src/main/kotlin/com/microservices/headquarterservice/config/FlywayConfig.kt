package com.microservices.headquarterservice.config

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
    private val headquarterServiceProperties: HeadquarterServiceProperties
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        return Flyway(
            Flyway
                .configure()
                .dataSource(
                    headquarterServiceProperties.spring.flyway.url,
                    headquarterServiceProperties.spring.flyway.user,
                    headquarterServiceProperties.spring.flyway.password
                )
        )
    }
}
