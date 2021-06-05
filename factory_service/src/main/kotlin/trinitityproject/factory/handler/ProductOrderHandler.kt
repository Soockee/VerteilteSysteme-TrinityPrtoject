package trinitityproject.factory.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

@Component
class ProductOrderHandler(private val repository: ProductOrderRepository) {

    @Suppress("UNUSED_PARAMETER")
    suspend fun getProductOrderServices(request: ServerRequest): ServerResponse {
        val productOrders: Flow<ProductOrder> = repository.findAll().asFlow()
        return ok().contentType(APPLICATION_JSON).bodyAndAwait(productOrders)
    }

    suspend fun getProductOrderService(request: ServerRequest): ServerResponse {
        val productOrderId = UUID.fromString(request.pathVariable("id"))
        val productOrder = repository
            .findById(productOrderId)
            .asFlow()
        if (productOrder.count() < 1) {
            return ServerResponse
                .notFound()
                .buildAndAwait()
        }
        return ok()
            .contentType(APPLICATION_JSON)
            .bodyAndAwait(productOrder)
    }

    suspend fun createProductOrderService(request: ServerRequest): ServerResponse {
        val productOrder = request.awaitBody<ProductOrder>()
        repository
            .save(productOrder)
            .subscribe()
        return ok().buildAndAwait()
    }
}