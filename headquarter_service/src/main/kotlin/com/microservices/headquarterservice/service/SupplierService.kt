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


    // fixed supplier id
    var supplierId: UUID = UUID.randomUUID()
    val logger = LoggerFactory.getLogger(ConditionService::class.java)

    /**
     * create a supplier
     * @return Returns all Supplier.
     */
    fun create(supplier: Supplier): Mono<Supplier> {
        return repository.save(supplier)
    }

    /**
     * get all supplier
     * @return Returns all Supplier.
     */
    fun getAll(): Flux<Supplier> {
        return repository.findAll()
    }

    /**
     * get all supplierorder
     * @return Returns all SupplierOrder.
     */
    fun getAllOrders(): Flux<SupplierOrder> {
        return orderRepository.findAll()
    }

    /**
     * get all orderparts
     * @param supplier_order_id A supplier_order_id for reference.
     * @return Returns all SupplierOrderPart.
     */
    fun getAllOrderParts(): Flux<SupplierOrderPart> {
        return orderPartRepository.findAll()
    }

    /**
     * create a supplierOrder
     * @return Returns created SupplierOrder.
     */
    fun create(): Mono<SupplierOrder> {
        val supplierOrder = SupplierOrder(
            null,
            supplierId,
            Instant.now(),
            "uncomplete"
        )
        return orderRepository.save(supplierOrder)
    }

    /**
     * create a supplierOrderPart
     * @param part_id A part id .
     * @param count A number of parts to be ordered.
     * @param supplier_order_id A supplier_order_id for reference.
     * @return Returns SupplierOrderResponse.
     */
    fun createSupplierOrderPart(part_id: UUID, count: Int, supplier_order_id: UUID): Mono<SupplierOrderPart> {
        val supplierOrderPart = SupplierOrderPart(
            null,
            supplier_order_id, // get order id
            part_id,
            count
        )
        return orderPartRepository.save(supplierOrderPart)
    }

    /**
     * create a SupplierOrder and its productOrderParts
     * and schedule the processing task
     * @param order A SupplierOrderRequest to create a order.
     * @return Returns SupplierOrderResponse.
     */
    fun createOrderByRequest(order: SupplierOrderRequest): Mono<SupplierOrderResponse> {
        val savedOrder = create().block()!!
        var isError = false
        for (productOrderPartRequest in order.supplierOrders) {
            var part = partRepository.findById(productOrderPartRequest.part_id).block()!!
            var condition_per_part =
                conditionRepository.findAll().filter { e -> e.part_id == part.part_id }.collectList().block()!!
            if (condition_per_part.isEmpty()) {
                logger.warn("BAD Part_ID IN create Supplier order: ${productOrderPartRequest.part_id}")
                isError = true
                break;
            } else {
                for (productOrderPartRequest in order.supplierOrders) {
                    createSupplierOrderPart(
                        productOrderPartRequest.part_id,
                        productOrderPartRequest.count,
                        savedOrder.order_id!!
                    ).block()!!
                }
            }
        }
        if (isError) {
            orderRepository.delete(savedOrder)
            return Mono.error(BadRequestException("Supplier Order does contain invalid parts"))
        } else {
            // Task for status update
            //supplierPartTaks.scheduleTaskWithDelay(savedOrder)
            return Mono.just(SupplierOrderResponse(savedOrder.order_id!!))
        }
    }

    /**
     * create a SupplierOrder and send to queue
     * @param order A order request to create a order and send.
     * @return Returns message of success of failure.
     */
    fun createOrderByRequestAndSend(order: SupplierOrderRequest): Mono<String> {
        createOrderByRequest(order)
            .switchIfEmpty(Mono.error(BadRequestException("Order unsuccessful")))
            .publishOn(Schedulers.boundedElastic())
            .map { e -> send(e) }
            .toFuture()
        return Mono.just("Supplier Order successful")
    }

    /**
     * get Status of SupplierOrder by id
     * @param supplier_order A supplier order id  to retrieve supplierorder status.
     * @return Returns supplierorder status.
     */
    fun getOrderStatus(supplier_order: UUID): Mono<SupplierOrderStatusResponse> {
        return orderRepository.findById(supplier_order).map { e -> SupplierOrderStatusResponse(e.status) }
    }

    /**
     * get Status of SupplierOrder by id
     * @param supplierOrderResponse A supplier order id to be send.
     */
    fun send(supplierOrderResponse: SupplierOrderResponse) {
        rabbitTemplate.convertAndSend(
            headquarterSupplierResponseQueue, Json.encodeToString(supplierOrderResponse)
        )
        logger.info("Send msg = " + supplierOrderResponse)
    }
}