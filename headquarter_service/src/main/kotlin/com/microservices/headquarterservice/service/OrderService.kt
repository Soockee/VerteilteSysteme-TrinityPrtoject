package com.microservices.headquarterservice.service

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
        logger.warn("Customer ID: " + orderRequest.customer_id)
        var order = Order(null, orderRequest.customer_id, null, "uncomplete")
        logger.warn(order.toString())

        return orderRepository.save(order)

        /*return orderRepository.save(order).flatMap { e ->
            orderRequest.products.forEach { orderProduct ->
                orderProductRepository.save(OrderProduct(null, e.order_id, orderProduct.product_id, orderProduct.count))
            }
            Mono.just(e)
        }*/
    }

    fun getAllOrders(): Flux<Order> {
        return orderRepository.findAll()
    }

    fun getAllOrderProducts(): Flux<OrderProduct> {
        return orderProductRepository.findAll()
    }

    fun getOrderProductsByOrderId(orderId: UUID): Flux<OrderProduct> {
        return orderProductRepository.findAll().filter { elem -> elem.order_id == orderId }
    }


    fun sendOrder(order: Order) {
        /**
         * order: {
        customerId: UUID
        products:[{
        productId: UUID
        count: Number (1-n)
        productionTime: Number (1-n seconds)
        parts: [
        partId: UUID
        count: Number (1-n)
        ]
        }
        ]
        }



        rabbitTemplate.convertAndSend(
        headquarterExchangeName,
        headquarterRoutingKey,
        Json.encodeToString(orderResponse))*/
    }
}