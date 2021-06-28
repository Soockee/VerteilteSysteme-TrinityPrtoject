package trinityproject.support.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties
@ConstructorBinding
data class SupportServiceProperties (
    val spring: Spring
)

data class Spring(
    val r2dbc: R2dbc,
    val flyway: Flyway
)

data class R2dbc(
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
    val url: String
)

data class Flyway(
    val url: String,
    val user: String,
    val password: String
)