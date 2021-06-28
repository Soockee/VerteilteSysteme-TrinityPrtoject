package trinitityproject.factory.service

import org.slf4j.Logger
import org.springframework.stereotype.Service
import trinitityproject.factory.repository.ProductOrderRepository
import java.time.Duration
import java.util.*
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.core.ParameterizedTypeReference
import trinitityproject.factory.model.*


@Service
class ReportService(
    private val repository: ProductOrderRepository,
    private val timeService: TimeService,
) {
    private val logger: Logger = LoggerFactory.getLogger(ReportService::class.java)

    private val costPerHour = 1

    /**
     * Generates a report consisting of the following data:
     *  - Number of orders since midnight until now
     *  - Number of goods produced since midnight until now
     *  - Cost of finished goods (cost of parts + production time)
     *
     * @return report
     */
    fun generateReport(): Report {
        val productOrdersMono = repository.findAll().collectList()

        val productOrders: List<ProductOrder> =
            productOrdersMono.block(Duration.ofMillis(1000)) as List<ProductOrder>;

        val finishedProductsCosts: Map<Product, Double> = getFinishedProductsCostsToday(productOrders);

        val report = Report(
            getProductOrdersToday(productOrders).size,
            finishedProductsCosts.size,
            calculateCosts(finishedProductsCosts)
        );

        logger.info(
            "new report created at " +
                    "${timeService.getVirtualCurrentLocalTime(System.currentTimeMillis())}: " +
                    "${report.toString()}"
        )

        return report;
    }

    /**
     * Calculates the cost of finished goods (cost of parts + production time)
     *
     * @return Cost of finished goods
     */
    fun calculateCosts(productsCost: Map<Product, Double>): Number {
        var partCosts = 0.0
        var productionTime = 0.0

        productsCost.keys.forEach { product ->
            partCosts += productsCost[product]!!
            productionTime += product.productData.productionTime
        }

        return partCosts + productionTime * costPerHour;
    }

    /**
     * Gets the orders received since midnight
     *
     * @return Orders received since midnight
     */
    fun getProductOrdersToday(productOrders: List<ProductOrder>): List<ProductOrder> {
        return productOrders.filter { productOrder -> timeService.isSameVirtualLocalDate(productOrder.receptionTime) }
    }

    /**
     * Gets the finished goods since midnight
     *
     * @return Finished goods since midnight
     */
    fun getFinishedProductsCostsToday(productOrders: List<ProductOrder>): Map<Product, Double> {
        var finishedProductsCosts: MutableMap<Product, Double> = HashMap()

        productOrders.forEach { productOrder ->
            productOrder.products.forEach { product ->
                if (product.status == Status.DONE && timeService.isSameVirtualLocalDate(product.completionTime)) {
                    var productPartCosts = 0.0
                    product.parts.forEach { part ->
                        productOrder.partOrders.forEach { partOrder ->
                            var position = partOrder.positions.find { position ->
                                position.partSupplierId == part.partId
                            }
                            if (position != null) productPartCosts += position!!.condition.price.toDouble()
                        }
                    }
                    finishedProductsCosts[product] = productPartCosts
                }
            }
        }

        return finishedProductsCosts
    }
}