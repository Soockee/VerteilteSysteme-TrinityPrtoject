package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.exception.BadRequestException
import com.microservices.headquarterservice.model.*
import com.microservices.headquarterservice.persistence.OrderProductRepository
import com.microservices.headquarterservice.persistence.OrderRepository
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
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val rabbitTemplate: AmqpTemplate,
    @Value("\${microservice.rabbitmq.routingkey}") val headquarterRoutingKey: String,
    @Value("\${microservice.rabbitmq.queue}") val headquarterQueueName: String,
    @Value("\${microservice.rabbitmq.exchange}") val headquarterExchangeName: String,
) {

    companion object {
        val logger = LoggerFactory.getLogger(ConditionService::class.java)
    }

    /*
    {
        "customerId": "ID.....",
        "products": [
            {
                "product_id": "ID....",
                "count": 5
            },
            {
                "product_id": "ID....",
                "count": 5
            },
        ]
    }
     */
    fun createOrder(orderRequest: OrderRequest): Mono<Order> {
        var orderRaw = Order(null, orderRequest.customer_id, Instant.now(), "uncomplete")

        return orderRepository
            .save(orderRaw)
            .flatMap { ord ->
                logger.info("Save Order: order_id:" + ord.order_id + " begin: " + ord.begin_order + " customer_id: " + ord.customer_id + " status " + ord.status)
                orderRequest.products.forEach { orderProductRequest ->
                    orderProductRepository.save(
                        OrderProduct(
                            null,
                            ord.order_id,
                            orderProductRequest.product_id,
                            orderProductRequest.count
                        )
                    ).subscribe { e ->
                        logger.info("Save OrderProduct: order_product_id: " + e.order_product_id + " product_id:" + e.product_id + " order_id:" + e.order_id)
                    }
                }
                Mono.just(ord)
            }
    }

    fun createCustomerOrder(orderRequest: OrderRequest): Mono<String> {
        return createOrder(orderRequest)
            .flatMap { order ->
            getOrderProductsByOrderId(order.order_id!!)
                .publishOn(Schedulers.boundedElastic())
                .collectList()
                .doOnNext { orderProductList ->
                    send(OrderResponse(order.customer_id, orderProductList))
                }.toFuture()
            Mono.just("Order successful with order_id: " + order.order_id)
        }.switchIfEmpty(Mono.error(BadRequestException("Order unsuccessful")))
    }

    fun getAllOrders(): Flux<Order> {
        return orderRepository.findAll()
    }

    fun getAllOrderProducts(): Flux<OrderProduct> {
        return orderProductRepository.findAll()
    }

    fun getOrderById(orderId: UUID): Mono<Order> {
        return orderRepository.findById(orderId)
    }

    fun getStatusById(orderId: UUID): Mono<String> {
        return orderRepository.findById(orderId).map { order -> order.status }
    }


    fun getOrderProductsByOrderId(orderId: UUID): Flux<OrderProduct> {
        return orderProductRepository.findAll().filter { elem -> elem.order_id == orderId }
    }


    fun send(orderResponse: OrderResponse) {
        rabbitTemplate.convertAndSend(
            headquarterExchangeName, headquarterRoutingKey, Json.encodeToString(orderResponse)
        )
        logger.info("Send msg = " + orderResponse)

    }
}