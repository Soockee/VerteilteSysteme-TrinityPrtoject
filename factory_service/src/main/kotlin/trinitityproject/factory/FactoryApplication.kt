package trinitityproject.factory

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
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
fun myQueue(): Queue {
	return Queue("myqueue")
}

@RabbitListener(queues = ["myqueue"])
fun processMessage(content: String?) {

}