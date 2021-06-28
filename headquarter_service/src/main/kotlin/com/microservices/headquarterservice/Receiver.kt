package com.microservices.headquarterservice

import com.microservices.headquarterservice.model.supplier.*
import com.microservices.headquarterservice.service.ConditionService
import com.microservices.headquarterservice.service.SupplierService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class Receiver(
    private val conditionService: ConditionService,
    private val supplierService: SupplierService,
    ) {
    companion object {
        val logger = LoggerFactory.getLogger(Receiver::class.java)
    }


//    @RabbitListener(queues = ["\${microservice.rabbitmq.queueOrder}"])
//    fun receiveOrder(@Payload request: ConditionResponse) {
//        logger.warn("receiveOrder: \n" +  "partid: "+request.part_id  + "\n" + "order list: ")
//        request.conditions.forEach{
//            e ->
//            logger.warn(
//                "condition_id: " + e.conditions_id + "\n" +
//                "part_id: " + e.part_id + "\n" +
//                "supplier_id: " + e.supplier_id + "\n" +
//                "price: " + e.price + "\n" +
//                "currency" + e.currency + "\n" +
//                "negotiation_timestamp: " + e.negotiation_timestamp+ "\n"
//            )
//        }
//    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueKIP}"])
    fun receiveKip(@Payload request: String) {
        logger.warn("receiveKip: " + request)
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueSupplier}"])
    fun receiveSupplier(@Payload request: SupplierOrderRequest): SupplierOrderResponse {
        request.supplierOrders.forEach{
                e ->
            logger.warn(
                "order_id: " + e.part_id + "\n"
            )
        }
        return supplierService.createOrderByRequest(request).block()!!
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueSupplierStatus}"])
    fun receiveSupplierStatus(@Payload request: SupplierOrderStatusRequest): SupplierOrderStatusResponse {
        return supplierService.getOrderStatus(request.order_id).block()!!
    }

//    @RabbitListener(queues = ["\${microservice.rabbitmq.queueCondition}"])
//    fun receiveCondition(@Payload request: ConditionRequest): ConditionResponse {
//        logger.warn("receiveCondition: " + request)
//        return conditionService.getByPartId(request.partId.toString())
//        .publishOn(Schedulers.boundedElastic())
//        .collectList()
//        .flatMap { conditionList ->
//            Mono.just(ConditionResponse(request.partId, conditionList))
//        }.block()!!
//    }
}