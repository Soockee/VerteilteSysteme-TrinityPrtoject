package com.microservices.headquarterservice.service


import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.model.headquarter.ConditionResponse
import com.microservices.headquarterservice.model.headquarter.Supplier
import com.microservices.headquarterservice.model.supplier.*
import com.microservices.headquarterservice.persistence.SupplierOrderPartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderRepository
import com.microservices.headquarterservice.persistence.SupplierRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant
import java.util.*

@Service
class SupplierService(
    private val repository: SupplierRepository,
    private val orderRepository: SupplierOrderRepository,
    private val orderPartRepository: SupplierOrderPartRepository,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.queueSupplierResponse}") val headquarterSupplierResponseQueue: String,

    ) {
    companion object {
        var supplierId: UUID = UUID.randomUUID()
        val logger = LoggerFactory.getLogger(ConditionService::class.java)

    }

    fun create(supplier: Supplier): Mono<Supplier> {
        return repository.save(supplier)
    }

    fun getAll(): Flux<Supplier> {
        return repository.findAll()
    }

    fun getAllOrders(): Flux<SupplierOrder> {
        return orderRepository.findAll()
    }

    fun create(): Mono<SupplierOrder> {
        val supplierOrder = SupplierOrder(
            null,
            supplierId,
            UUID.randomUUID(), // factory id
            Instant.now(),
            Instant.now(), // negotiation timestamp
            "uncomplete"
        )
        return orderRepository.save(supplierOrder)
    }

    fun createSupplierOrderPart(part_id: UUID, count: Int, supplier_order_id: UUID): Mono<SupplierOrderPart> {
        val supplierOrderPart = SupplierOrderPart(
            null,
            supplier_order_id, // get order id
            part_id,
            count
        )
        return orderPartRepository.save(supplierOrderPart)
    }

    fun createOrderByRequest(order: SupplierOrderRequest): Mono<SupplierOrderResponse> {
        val savedOrder = create()
        savedOrder
            .publishOn(Schedulers.boundedElastic())
            .map { savedOrderItem ->
                for (productOrderPartRequest in order.supplierOrders) {
                    createSupplierOrderPart(productOrderPartRequest.part_id, productOrderPartRequest.count, savedOrderItem.order_id!!)
                }
            }
            .toFuture()
        savedOrder.flatMap { e -> Mono.just(e.order_id!!) }
        return savedOrder.flatMap { e -> Mono.just(SupplierOrderResponse(e.order_id!!)) }
    }
    fun createOrderByRequestAndSend(order: SupplierOrderRequest): Mono<String> {
        createOrderByRequest(order)
            .switchIfEmpty(Mono.error(BadRequestException("Order unsuccessful")))
            .publishOn(Schedulers.boundedElastic())
            .map { e-> send(e) }
            .toFuture()
        return Mono.just("Supplier Order successful")
    }
    fun getOrderStatus(supplier_order: UUID): Mono<SupplierOrderStatusResponse> {
        return orderRepository.findById(supplier_order).map { e -> SupplierOrderStatusResponse(e.status) }
    }
    fun send(supplierOrderResponse: SupplierOrderResponse) {
        rabbitTemplate.convertAndSend(
            headquarterSupplierResponseQueue, Json.encodeToString(supplierOrderResponse)
        )
        ConditionService.logger.info("Send msg = " + supplierOrderResponse)
    }
    /**
     * Fixed Delay after Creation:
     * 1) Update Status
     * 2) Report Status to MQ
     */

}