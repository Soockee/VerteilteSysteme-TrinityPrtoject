package com.microservices.projectservice.config

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
    private val projectServiceProperties: ProjectServiceProperties
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        return Flyway(
            Flyway
                .configure()
                .dataSource(
                    projectServiceProperties.spring.flyway.url,
                    projectServiceProperties.spring.flyway.user,
                    projectServiceProperties.spring.flyway.password
                )
        )
    }
}
