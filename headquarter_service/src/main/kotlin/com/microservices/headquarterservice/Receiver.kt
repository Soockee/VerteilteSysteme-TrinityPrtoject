package com.microservices.headquarterservice

import com.microservices.headquarterservice.model.headquarter.condition.ConditionRequest
import com.microservices.headquarterservice.model.headquarter.condition.ConditionResponse
import com.microservices.headquarterservice.model.factory.kpi.Report
import com.microservices.headquarterservice.model.supplier.*
import com.microservices.headquarterservice.service.ConditionService
import com.microservices.headquarterservice.service.KPIService
import com.microservices.headquarterservice.service.SupplierService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


@Component
class Receiver(
    private val conditionService: ConditionService,
    private val kpiService: KPIService
) {
    val logger = LoggerFactory.getLogger(Receiver::class.java)

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueKPI}"])
    fun receiveKPI(@Payload report: Report) {
        logger.info("received new kpi report from factory " + report.factoryName + ": " + report.toString())
        kpiService.create(report).block()!!
    }

    @RabbitListener(queues = ["\${microservice.rabbitmq.queueCondition}"])
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