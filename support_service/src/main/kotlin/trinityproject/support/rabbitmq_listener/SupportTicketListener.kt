package trinityproject.support.rabbitmq_listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import trinityproject.support.model.support.SupportTicketResponse
import trinityproject.support.service.SupportTicketService


@Component
class SupportTicketListener(
    private val supportTicketService: SupportTicketService
) {
    val logger: Logger = LoggerFactory.getLogger(SupportTicketListener::class.java)

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

    @RabbitListener(queues = ["support"])
    @Throws(InterruptedException::class)
    fun receiveTicket(@Payload supportTicketResponse: SupportTicketResponse) {
        logger.warn("KEKW")
        logger.warn("Support Ticket Response: $supportTicketResponse")
    }
}