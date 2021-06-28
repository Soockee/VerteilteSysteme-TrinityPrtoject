package trinityproject.support.config

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig (
    private val supportServiceProperties: SupportServiceProperties
){

    @Bean
    fun flyway(): Flyway {
        return Flyway(Flyway.configure().dataSource(
            supportServiceProperties.spring.flyway.url,
            supportServiceProperties.spring.flyway.user,
            supportServiceProperties.spring.flyway.password))
    }
}