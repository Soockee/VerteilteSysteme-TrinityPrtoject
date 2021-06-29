package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.common.Part
import com.microservices.headquarterservice.model.factory.kpi.Report
import com.microservices.headquarterservice.model.headquarter.condition.ConditionResponse
import com.microservices.headquarterservice.persistence.KPIRepository
import com.microservices.headquarterservice.persistence.PartRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class KPIService(
    private val repository: KPIRepository,
    @Value("\${microservice.rabbitmq.queueOrderUSA}") val orderQueueUSA: String,
    @Value("\${microservice.rabbitmq.queueOrderChina}") val orderQueueChina: String,
){
    fun create(report: Report): Mono<Report> {
        return repository.save(report)
    }

    fun getAll(): Flux<Report> {
        return repository.findAll()
    }

    fun getFactoryLowestLoad(): String {
        val loadUSA = getLatestReportByFactory("usa")?.orderCount
            .takeUnless { it == -1 } ?: Int.MAX_VALUE
        val loadChina = getLatestReportByFactory("china")?.orderCount
            .takeUnless { it == -1 } ?: Int.MAX_VALUE

        return if (loadUSA.toInt() >= loadChina.toInt()) orderQueueChina else orderQueueUSA
    }

    private fun getLatestReportByFactory(factoryName: String): Report? {
        var factoryReports = getAll().filter { report ->
            report.factoryName == factoryName
        }.collectList().block()

        if (factoryReports != null) {
            return factoryReports[factoryReports.lastIndex]
        }

        return null
    }
}