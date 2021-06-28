package com.microservices.headquarterservice.service


import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.model.headquarter.Supplier
import com.microservices.headquarterservice.model.supplier.*
import com.microservices.headquarterservice.persistence.*
import com.microservices.headquarterservice.tasks.SupplierPartTask
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
    private val partRepository: PartRepository,
    private val conditionRepository: ConditionRepository,
    private val orderPartRepository: SupplierOrderPartRepository,
    private val rabbitTemplate: AmqpTemplate,
    private val supplierPartTaks: SupplierPartTask,
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
    fun getAllOrderParts(): Flux<SupplierOrderPart> {
        return orderPartRepository.findAll()
    }


    fun create(): Mono<SupplierOrder> {
        val supplierOrder = SupplierOrder(
            null,
            supplierId,
            Instant.now(),
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
        val savedOrder = create().block()!!
        var isError = false
        for (productOrderPartRequest in order.supplierOrders) {
            var part = partRepository.findById(productOrderPartRequest.part_id).block()!!
            var condition_per_part = conditionRepository.findAll().filter { e -> e.part_id == part.part_id }.collectList().block()!!
            if (condition_per_part.isEmpty()) {
                logger.warn("BAD Part_ID IN create Supplier order: ${productOrderPartRequest.part_id}")
                isError = true
                break;
            }
            else{
                for (productOrderPartRequest in order.supplierOrders) {
                    createSupplierOrderPart(
                        productOrderPartRequest.part_id,
                        productOrderPartRequest.count,
                        savedOrder.order_id!!
                    ).block()!!
                }
            }
        }
        if(isError){
            orderRepository.delete(savedOrder)
            return Mono.error(BadRequestException("Supplier Order does contain invalid parts"))
        }
        else{
            // Task for status update
            supplierPartTaks.scheduleTaskWithDelay(savedOrder)
            return Mono.just(SupplierOrderResponse(savedOrder.order_id!!))
        }
    }

    fun createOrderByRequestAndSend(order: SupplierOrderRequest): Mono<String> {
        createOrderByRequest(order)
            .switchIfEmpty(Mono.error(BadRequestException("Order unsuccessful")))
            .publishOn(Schedulers.boundedElastic())
            .map { e -> send(e) }
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