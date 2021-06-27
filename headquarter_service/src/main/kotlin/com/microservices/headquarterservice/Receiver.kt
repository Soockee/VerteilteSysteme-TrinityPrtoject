package com.microservices.headquarterservice

import com.microservices.headquarterservice.model.headquarter.ConditionRequest
import com.microservices.headquarterservice.model.headquarter.ConditionResponse
import com.microservices.headquarterservice.service.ConditionService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Component
class Receiver(
    private val conditionService: ConditionService,
    ) {
    companion object {
        val logger = LoggerFactory.getLogger(Receiver::class.java)
    }


    @RabbitListener(queues = ["\${microservice.rabbitmq.queueOrderRequests}"])
    fun receiveOrder(@Payload request: ConditionResponse) {
        logger.warn("receiveOrder: \n" +  "partid: "+request.part_id  + "\n" + "order list: ")
        request.conditions.forEach{
            e ->
            logger.warn(
                "condition_id: " + e.conditions_id + "\n" +
                "part_id: " + e.part_id + "\n" +
                "supplier_id: " + e.supplier_id + "\n" +
                "price: " + e.price + "\n" +
                "currency" + e.currency + "\n" +
                "negotiation_timestamp: " + e.negotiation_timestamp+ "\n"
            )
        }
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueKIPRequests}"])
    fun receiveKip(@Payload request: String) {
        logger.warn("receiveKip: " + request)
        //  conditionService.getByPartId(request.partId.toString())
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueConditionRequests}"])
    fun receiveCondition(@Payload request: ConditionRequest): ConditionResponse {
        logger.warn("receiveCondition: " + request)
        return conditionService.getByPartId(request.partId.toString())
        .publishOn(Schedulers.boundedElastic())
        .collectList()
        .flatMap { conditionList ->
            Mono.just(ConditionResponse(request.partId, conditionList))
        }.block()!!
    }
}