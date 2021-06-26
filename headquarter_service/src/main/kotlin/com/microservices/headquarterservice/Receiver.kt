package com.microservices.headquarterservice

import com.microservices.headquarterservice.model.Condition
import com.microservices.headquarterservice.model.ConditionRequest
import org.springframework.stereotype.Component;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.handler.annotation.Payload
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Component
class Receiver {
    companion object {
        val logger = LoggerFactory.getLogger(Receiver::class.java)
    }

    @RabbitListener(queuesToDeclare = [Queue(name = "\${microservice.rabbitmq.queue}", durable = "true")])
    fun receiveOrder(@Payload request: ConditionRequest):Condition {
        logger.warn("Received <$request>");
         return Condition(
            conditions_id = UUID.randomUUID(),
            supplier_id = UUID.randomUUID(),
            part_id = UUID.randomUUID(),
            price = BigDecimal(400),
            currency = "EURO",
            negotiation_timestamp = Instant.now()
        )
    }
}