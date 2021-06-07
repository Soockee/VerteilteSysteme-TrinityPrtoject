package trinitityproject.factory.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import trinitityproject.factory.messaging.DestinationsConfig
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*
import javax.annotation.PostConstruct


@Component
class ProductOrderHandler(
    private val repository: ProductOrderRepository,
    private val destinationsConfig: DestinationsConfig,
    private val amqpAdmin: AmqpAdmin,
    private val amqpTemplate: AmqpTemplate
) {
    private val log: Logger = LoggerFactory.getLogger(ProductOrderHandler::class.java)

    @PostConstruct
    fun setupQueueDestinations() {
        //Setup of the quueues defined in application.properties
        destinationsConfig.getQueues()
            .forEach { (key: String?, destination: DestinationsConfig.DestinationInfo) ->
                log.info(
                    "[I54] Creating directExchange: key={}, name={}, routingKey={}",
                    key,
                    destination.exchange,
                    destination.routingKey
                )
                val ex: Exchange = ExchangeBuilder.directExchange(
                    destination.exchange
                )
                    .durable(true)
                    .build()
                amqpAdmin.declareExchange(ex)
                val q = QueueBuilder.durable(
                    destination.routingKey
                )
                    .build()
                amqpAdmin.declareQueue(q)
                val b = BindingBuilder.bind(q)
                    .to(ex)
                    .with(destination.routingKey)
                    .noargs()
                amqpAdmin.declareBinding(b)
            }
    }

    @RabbitListener(queuesToDeclare = [Queue("nyse")])
    @Throws(InterruptedException::class)
    fun receive1(msg: String?) {
        //Declare listener to queue
        val d = destinationsConfig.getQueues()["IBOV"]
        if (msg != null && d != null) {
            log.info("Message received:", msg.toString())
            amqpTemplate.convertAndSend(
                d.exchange,
                d.routingKey,
                "Hello from Factory"
            )
        }
    }

    @Suppress("UNUSED_PARAMETER")
    suspend fun sendToMessageBus(request: ServerRequest): ServerResponse {
        //Send message to queue
        val d = destinationsConfig.getQueues()["NYSE"]

        return if (d == null) {
            ServerResponse
                .notFound()
                .buildAndAwait()
        } else {
            val response = Mono.fromCallable {
                amqpTemplate.convertAndSend(
                    d.exchange,
                    d.routingKey,
                    "Send Message from factory get-route"
                )
                ResponseEntity.accepted().build<Any?>()
            }.asFlow()
            log.info("sendMessage")
            return ok().contentType(APPLICATION_JSON).bodyAndAwait(response)
        }
    }

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