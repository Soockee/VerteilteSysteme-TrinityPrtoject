package com.microservices.headquarterservice

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
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
    private val rabbitTemplate: AmqpTemplate,
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

    @Component
    class ReceiverConfig(
        @Value("\${microservice.rabbitmq.routingkey_order}") val routingkey_order: String,
        @Value("\${microservice.rabbitmq.routingkey_kip}") val routingkey_kip: String,
        @Value("\${microservice.rabbitmq.routingkey_condition}") val routingkey_condition: String,
        private val queueKIP: Queue,
        private val queueOrder: Queue,
        private val queueCondition: Queue,
        private val exchange: TopicExchange,
        private val amqpAdmin: AmqpAdmin,
    ) {
        @PostConstruct
        fun createQueues() {
            amqpAdmin!!.declareQueue(queueKIP)
            amqpAdmin!!.declareQueue(queueOrder)
            amqpAdmin!!.declareQueue(queueCondition)
            bindQueuesToKey()
        }

        fun bindQueuesToKey() {
            amqpAdmin.declareBinding(bind(queueOrder, exchange, routingkey_order))
            amqpAdmin.declareBinding(bind(queueCondition, exchange, routingkey_condition))
            amqpAdmin.declareBinding(bind(queueKIP, exchange, routingkey_kip))
        }

        fun bind(queueOrder: Queue, exchange: TopicExchange, key: String): Binding {
            return BindingBuilder.bind(queueOrder).to(exchange).with(key)
        }
    }

}

