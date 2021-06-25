package trinitityproject.factory.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import trinitityproject.factory.handler.ProductOrderHandler
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.repository.ProductOrderRepository

@Component
class ProductOrderService(
    private val repository: ProductOrderRepository,
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    fun createOrder(order: ProductOrder) {
        repository.save(order).block()
        log.info("Saved to order to database: $order")
    }

}