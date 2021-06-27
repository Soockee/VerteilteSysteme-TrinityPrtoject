package com.microservices.headquarterservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Configuration
class RabbitConfig(
    @Value("\${microservice.rabbitmq.queueConditionRequests}") val queueConditionRequests: String,
    @Value("\${microservice.rabbitmq.queueKIPRequests}") val queueKIP: String,
    @Value("\${microservice.rabbitmq.queueOrderRequests}") val queueOrderRequests: String,
    @Value("\${microservice.rabbitmq.exchange}") val headquarterExchangeName: String,
    private val connectionFactory: ConnectionFactory
) {
    companion object {
        val logger = LoggerFactory.getLogger(HeadquarterServiceApplication::class.java)
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(headquarterExchangeName)
    }

    @Bean
    fun queueOrder(): Queue {
        // logger.warn(headquarterQueueName)
        return Queue(queueOrderRequests, true)
    }

    @Bean
    fun queueKIP(): Queue {
        // logger.warn(headquarterQueueName)
        return Queue(queueKIP, true)
    }

    @Bean
    fun queueCondition(): Queue {
        // logger.warn(headquarterQueueName)
        return Queue(queueConditionRequests, true)
    }


    @Bean
    fun amqpAdmin(): AmqpAdmin? {
        return RabbitAdmin(connectionFactory)
    }
    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): RabbitListenerContainerFactory<*>? {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        // A custom objectMapper is needed, so the default values are set by kotlin
        val mapper = ObjectMapper()
            .registerModules(
                KotlinModule(),
                JavaTimeModule(),
            )
//        val mapper = JsonMapper.builder()
//            .findAndAddModules()
//            .build()
        factory.setMessageConverter(Jackson2JsonMessageConverter(mapper))
        return factory
    }

    @Component
    class ReceiverConfig(
        private val queueKIP: Queue,
        private val queueOrder: Queue,
        private val queueCondition: Queue,
        private val amqpAdmin: AmqpAdmin,
    ) {
        @PostConstruct
        fun createQueues() {
            amqpAdmin!!.declareQueue(queueKIP)
            amqpAdmin!!.declareQueue(queueOrder)
            amqpAdmin!!.declareQueue(queueCondition)
           // bindQueuesToKey()
        }

    }
}

