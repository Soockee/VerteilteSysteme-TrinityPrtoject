package com.microservices.headquarterservice

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
    fun queue(): Queue {
        // logger.warn(headquarterQueueName)
        return Queue(headquarterQueueName, false)
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
    fun container(
            connectionFactory: ConnectionFactory,
            listenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {
        var container: SimpleMessageListenerContainer = SimpleMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.setQueueNames(headquarterQueueName)
        container.setMessageListener(listenerAdapter)
        return container
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
