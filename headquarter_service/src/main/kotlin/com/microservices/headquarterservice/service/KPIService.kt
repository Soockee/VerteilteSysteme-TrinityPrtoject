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
    /**
     * Save a report to the repository
     *
     * @return Mono with the report
     */
    fun create(report: Report): Mono<Report> {
        return repository.save(report)
    }

    /**
     * Get all reports
     *
     * @return Returns all reports.
     */
    fun getAll(): Flux<Report> {
        return repository.findAll()
    }

    /**
     * Get the name of the factory with the lowest load.
     * The load is calculated with the values of the kpi report.
     *
     * @return Name of the factory
     */
    fun getFactoryLowestLoad(): String {
        val reportUSA = getLatestReportByFactory("usa")
        val reportChina = getLatestReportByFactory("china")

        val loadUSA = calculateLoad(reportUSA)
        val loadChina = calculateLoad(reportChina)

        return if(loadUSA <= loadChina) "order/usa" else "order/china"
    }

    /**
     * Gets the latest report of the factory with the given name
     *
     * @return The report if any was found
     */
    private fun getLatestReportByFactory(factoryName: String): Report? {
        var factoryReports = getAll().filter { report ->
            report.factoryName == factoryName
        }.collectList().block()

        if (factoryReports != null && factoryReports.lastIndex != -1) {
            return factoryReports[factoryReports.lastIndex]
        }

        return null
    }

    /**
     * Calculates the load of a factory depending on the last report received from it.
     * The load is calculated by: load = open orders / productivity of the factory
     *
     * @return Returns all Parts.
     */
    private fun calculateLoad(report: Report?): Double {
        val productivity = report?.productivity?.toDouble().takeUnless { it == -1.0 } ?:  0.0
        val openOrders = report?.openOrders?.toDouble().takeUnless { it == -1.0 } ?: 0.0

        return openOrders / (productivity.takeUnless { it == 0.0 } ?: 1.0)
    }
}