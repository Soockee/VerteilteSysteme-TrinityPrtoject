package com.microservices.headquarterservice

import com.microservices.headquarterservice.service.ConditionService
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class Receiver{
    companion object {
        val logger = LoggerFactory.getLogger(Receiver::class.java)
    }


    @RabbitListener(queues = ["\${microservice.rabbitmq.queueOrderRequests}"])
    fun receiveOrder(@Payload request: String) {
        logger.warn("receiveOrder: " + request)
        //  conditionService.getByPartId(request.partId.toString())
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueKIPRequests}"])
    fun receiveKip(@Payload request: String) {
        logger.warn("receiveKip: " + request)
        //  conditionService.getByPartId(request.partId.toString())
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueConditionRequests}"])
    fun receiveCondition(@Payload request: String) {
        logger.warn("receiveCondition: " + request)
        //  conditionService.getByPartId(request.partId.toString())
    }
}