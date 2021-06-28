package trinitityproject.factory

import org.springframework.amqp.core.Queue
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
class FactoryApplication

fun main(args: Array<String>) {
    runApplication<FactoryApplication>(*args)
}

@Bean
fun queue(): Queue {
    return Queue("kpi")
}
