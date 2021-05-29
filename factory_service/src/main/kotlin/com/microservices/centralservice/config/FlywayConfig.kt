package com.microservices.centralservice.config

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
    private val centralServiceProperties: CentralServiceProperties
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        return Flyway(
            Flyway
                .configure()
                .dataSource(
                    centralServiceProperties.spring.flyway.url,
                    centralServiceProperties.spring.flyway.user,
                    centralServiceProperties.spring.flyway.password
                )
        )
    }
}
