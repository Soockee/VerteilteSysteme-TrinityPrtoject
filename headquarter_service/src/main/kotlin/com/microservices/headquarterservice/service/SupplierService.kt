package com.microservices.headquarterservice.service


import com.microservices.headquarterservice.model.headquarter.ConditionResponse
import com.microservices.headquarterservice.model.headquarter.Supplier
import com.microservices.headquarterservice.model.supplier.SupplierOrder
import com.microservices.headquarterservice.model.supplier.SupplierOrderResponse
import com.microservices.headquarterservice.persistence.SupplierOrderPartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderRepository
import com.microservices.headquarterservice.persistence.SupplierRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant

@Service
class SupplierService(
    private val repository: SupplierRepository,
    private val orderRepository: SupplierOrderRepository,
    private val orderPartrepository: SupplierOrderPartRepository,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.queueConditionRequests}") val headquarterSupplierQueue: String,
){
    fun create(supplier: Supplier): Mono<Supplier> {
        return repository.save(supplier)
    }

    fun getAll(): Flux<Supplier> {
        return repository.findAll()
    }

    fun create(order: SupplierOrder): Mono<SupplierOrder> {
        order.begin_order = Instant.now()
        return orderRepository.save(order)
    }

    fun createOrderAndUpdate(order: SupplierOrder): Mono<SupplierOrder> {
        val savedOrder = create(order)
        savedOrder.publishOn(Schedulers.boundedElastic())
            .map { conditionItem ->
                send(SupplierOrderResponse(order.order_id)) }
            .toFuture()
        return savedOrder
    }



    fun send(orderResponse: SupplierOrderResponse) {
        rabbitTemplate.convertAndSend(
            headquarterSupplierQueue, Json.encodeToString(orderResponse)
        )
        ConditionService.logger.info("Send msg = " + orderResponse)
    }

}