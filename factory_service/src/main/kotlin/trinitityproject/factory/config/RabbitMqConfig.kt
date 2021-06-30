package trinitityproject.factory.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMqConfig(
    @Value("\${factory.conditionQueueName}") private val conditionQueue: String,
    @Value("\${factory.orderQueueName}") private val orderQueue: String
) {

    @Bean
    fun conditionQueue(): Queue {
        return Queue(conditionQueue)
    }

    @Bean
    fun orderQueue(): Queue {
        return Queue(orderQueue)
    }

    // The jackson is set for the rabbitmq RPC-call
    @Bean
    fun jackson2MessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

}