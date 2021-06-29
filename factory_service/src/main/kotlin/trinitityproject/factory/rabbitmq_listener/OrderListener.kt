package trinitityproject.factory.rabbitmq_listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.service.ProductOrderService


@Component
class OrderListener(
    private val productOrderService: ProductOrderService
) {
    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): RabbitListenerContainerFactory<*>? {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        // A custom objectMapper is needed, so the default values are set by kotlin
        val mapper = ObjectMapper()
            .registerModule(KotlinModule())
        factory.setMessageConverter(Jackson2JsonMessageConverter(mapper))
        return factory
    }

    @RabbitListener(queuesToDeclare = [Queue(name = "order/china")])
    @Throws(InterruptedException::class)
    fun receiveOrder(@Payload order: ProductOrder) {
        productOrderService.createOrder(order)
    }
}