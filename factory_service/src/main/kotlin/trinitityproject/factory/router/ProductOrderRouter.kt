package trinitityproject.factory.router

import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter
import trinitityproject.factory.handler.ProductOrderHandler

@Configuration
class ProductOrderRouter {
    @RouterOperations(
        RouterOperation(
            path = "/productOrders",
            method = arrayOf(RequestMethod.GET),
            beanClass = ProductOrderHandler::class,
            beanMethod = "getProductOrderServices"
        ),
        RouterOperation(
            path = "/productOrder/{id}",
            method = arrayOf(RequestMethod.GET),
            beanClass = ProductOrderHandler::class,
            beanMethod = "getProductOrderService"
        ),
        RouterOperation(
            path = "/productOrder",
            method = arrayOf(RequestMethod.POST),
            beanClass = ProductOrderHandler::class,
            beanMethod = "createProductOrderService"
        )
    )
    @Bean
    fun router(handler: ProductOrderHandler) = coRouter {
        accept(APPLICATION_JSON).nest {
            "/productOrders".nest {
                GET("/", accept(APPLICATION_JSON), handler::getProductOrderServices)
            }
            "/productOrder".nest {
                GET("/{id}", accept(APPLICATION_JSON), handler::getProductOrderService)
                POST("/", accept(APPLICATION_JSON), handler::createProductOrderService)
            }
        }
    }
}
