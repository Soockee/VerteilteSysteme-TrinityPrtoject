package com.microservices.headquarterservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.microservices.headquarterservice.model.ConditionRequest
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.messaging.handler.annotation.Payload

@SpringBootApplication
@EnableR2dbcRepositories
@ConfigurationPropertiesScan("com.microservices.headquarterservice.config")
class HeadquarterServiceApplication(
    @Value("\${microservice.rabbitmq.routingkey}") val headquarterRoutingKey: String,
    @Value("\${microservice.rabbitmq.queue}") val headquarterQueueName: String,
    @Value("\${microservice.rabbitmq.exchange}") val headquarterExchangeName: String,
) {
    companion object {
        val logger = LoggerFactory.getLogger(HeadquarterServiceApplication::class.java)
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(headquarterExchangeName)
    }

    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(headquarterRoutingKey)
    }


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

    @Bean
    fun listenerAdapter(receiver: Receiver): MessageListenerAdapter {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }

}

fun main(args: Array<String>) {
    runApplication<HeadquarterServiceApplication>(*args)
}

// class Receiver {
//     fun receiveMessage(message: String) {
//       System.out.println("Received <" + message + ">");
//     }
//   }
