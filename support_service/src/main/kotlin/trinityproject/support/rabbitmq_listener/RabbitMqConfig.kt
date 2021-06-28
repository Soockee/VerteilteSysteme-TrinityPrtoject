package trinityproject.support.rabbitmq_listener

import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMqConfig {
    @Bean
    fun queue(): Queue {
        return Queue("support")
    }

    @Bean
    fun jackson2MessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }
}