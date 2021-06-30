package trinitityproject.factory.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TimeServiceConfig(
    @Value("\${factory.dayInMillis}") val dayInMillis: String
) {
    @Bean
    fun dayInMillis(): Long {
        return dayInMillis.toLong()
    }
}