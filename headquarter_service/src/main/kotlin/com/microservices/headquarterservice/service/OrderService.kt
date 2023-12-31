package com.microservices.headquarterservice.service

import com.microservices.headquarterservice.model.headquarter.order.*
import com.microservices.headquarterservice.model.headquarter.Product
import com.microservices.headquarterservice.model.headquarter.ProductPart
import com.microservices.headquarterservice.persistence.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productRepository: ProductRepository,
    private val productPartsRepository: ProductPartRepository,
    private val partRepository: PartRepository,
    private val rabbitTemplate: AmqpTemplate,
    private val kpiService: KPIService,
    @Value("\${microservice.rabbitmq.queueOrderUSA}") val orderQueueUSA: String,
    @Value("\${microservice.rabbitmq.queueOrderChina}") val orderQueueChina: String,
) {
    val logger = LoggerFactory.getLogger(OrderService::class.java)

    /**
     * create an order and its corresponding n-m relationship to product
     * @param orderRequest A order as orderRequest to be created.
     * @return Returns order.
     */
    fun createOrder(orderRequest: OrderRequest): Order {
        var orderRaw = Order(null, orderRequest.customer_id, Instant.now(), "uncomplete")
        val order = orderRepository.save(orderRaw).block()!!
        logger.warn("create:::: order  ${order.order_id} ---  ${order.customer_id}")
        val orderProductList = mutableListOf<OrderProduct>()
        for (product in orderRequest.products) {

            orderProductList.add(
                orderProductRepository.save(
                    OrderProduct(
                        null,
                        order.order_id,
                        product.product_id,
                        product.count
                    )
                ).block()!!
            )
            logger.warn("saving some stuff :::: order  ${order.order_id} ---  ${order.customer_id}")
        }
        sendCustomerOrder(order, orderProductList)
        return order
    }

    /**
     * send an order to order queue and retrieve n-m relationship
     * @param order A orderId to be send.
     * @param orderProducts A orderProducts (n-m table) for lookup.
     */
    fun sendCustomerOrder(order: Order, orderProducts: MutableList<OrderProduct>) {
        var products: MutableList<Product> = mutableListOf()
        var productParts: MutableList<ProductPart> = mutableListOf()
        logger.warn("order  ${order.order_id} ---  ${order.customer_id}")

        products.addAll(
            productRepository.findAll().collectList().block()!!
        )

        productParts.addAll(
            productPartsRepository.findAll().collectList().block()!!
        )

        // build responses
        var orderProductResponses: MutableList<OrderProductResponse> = mutableListOf()

        for (orderProduct in orderProducts) {
            for (product in products.filter { e -> e.product_id == orderProduct.product_id }) {
                var productPartsResponse: MutableList<ProductPart> = mutableListOf()
                for (productPart in productParts.filter { e -> e.product_id == product.product_id }) {
                    productPartsResponse.add(productPart)
                }
                orderProductResponses.add(
                    OrderProductResponse(
                        product,
                        orderProduct.count,
                        productPartsResponse
                    )
                )
            }
        }
        send(OrderResponse(order.customer_id, orderProductResponses))
    }

    /**
     * return all Order
     * @return Returns all orders.
     */
    fun getAllOrders(): Flux<Order> {
        return orderRepository.findAll()
    }

    /**
     * return all OrderProducts
     * @return Returns all OrderProducts.
     */
    fun getAllOrderProducts(): Flux<OrderProduct> {
        return orderProductRepository.findAll()
    }

    /**
     * get orderr by id
     * @param orderId A orderId to be created.
     * @return Returns order.
     */
    fun getOrderById(orderId: UUID): Mono<Order> {
        return orderRepository.findById(orderId)
    }

    /**
     * get status of oder by id
     * @param orderId A orderId to be created.
     * @return Returns status.
     */
    fun getStatusById(orderId: UUID): Mono<String> {
        return orderRepository.findById(orderId).map { order -> order.status }
    }

    /**
     * send orderResponse with properties
     *
     * the factory to send the order to is determined by comparing the load of the factories
     */
    fun send(orderResponse: OrderResponse) {
        val orderQueue: String = kpiService.getFactoryLowestLoad()
        logger.warn(orderQueue)
        rabbitTemplate.convertAndSend(
            orderQueue,
            Json.encodeToString(orderResponse) as Any
        ) { message ->
            message.messageProperties.contentType = "application/json"
            message.messageProperties.deliveryMode = MessageDeliveryMode.PERSISTENT
            message
        }
        logger.info("Send order = $orderResponse to queue $orderQueue")
    }
}