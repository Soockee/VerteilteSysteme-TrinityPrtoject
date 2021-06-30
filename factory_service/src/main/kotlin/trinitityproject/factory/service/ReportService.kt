package trinitityproject.factory.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import trinitityproject.factory.model.Product
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.model.Report
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import java.time.Duration
import java.util.*
import trinitityproject.factory.model.*
import java.time.temporal.ChronoUnit


@Service
class ReportService(
    private val repository: ProductOrderRepository,
    private val timeService: TimeService,
    @Value("\${factory.name}") val factoryName: String,
) {
    private val logger: Logger = LoggerFactory.getLogger(ReportService::class.java)

    private val costPerHour = 1

    /**
     * Generates a report consisting of the following data:
     *  The KPIs listed in the requirements document:
     *  - Number of orders since midnight until now
     *  - Number of goods produced since midnight until now
     *  - Cost of finished goods (cost of parts + production time)
     *
     *  Other performance indicators to determine which factory has a higher load:
     *  - Number of open product orders
     *  - Productivity of the factory (completed products / received products)
     *
     * @return report
     */
    fun generateReport(): Report {
        val productOrdersMono = repository.findAll().collectList()

        var productOrders: List<ProductOrder> =
            (productOrdersMono.block(Duration.ofMillis(1000)) as List<ProductOrder>)
                .filter { productOrder -> productOrder.factoryName == factoryName }

        val completedProductPartOrdersPairs = getCompletedProductPartOrdersPairs(productOrders);

        val completedProductPartOrdersPairsToday = completedProductPartOrdersPairs.filter { productPartOrdersPair ->
            timeService.isSameVirtualLocalDate(productPartOrdersPair.first.completionTime)
        }

        val report = Report(
            null,
            getProductOrdersToday(productOrders).size,
            completedProductPartOrdersPairs.size,
            getCompletedProductsCostsToday(completedProductPartOrdersPairsToday),
            countOpenOrders(productOrders),
            calculateFactoryProductivity(productOrders),
            factoryName,
            System.currentTimeMillis()
        );

        logger.info(
            "new report from $factoryName created at " +
                    "${timeService.getVirtualLocalTime(System.currentTimeMillis())}: " +
                    "${report.toString()}"
        )

        return report
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
     * Calculates the cost of all products completed on this day consisting of:
     * cost of the product parts + production time * production cost per hour
     *
     * @return Cost of products completed today
     */
    fun getCompletedProductsCostsToday(completedProductCostsToday: List<Pair<Product, List<PartOrder>>>): Double {
        return completedProductCostsToday.sumOf { productPartOrdersPair ->
            productPartOrdersPair.first.productData.productionTime * costPerHour +
            getPartCostsForProduct(productPartOrdersPair.first, productPartOrdersPair.second)
        }
    }

    /**
     * Calculates the cost of a products parts according to the conditions for these parts at the time of production
     *
     * @return Finished goods since midnight
     */
    fun getPartCostsForProduct(product: Product, partOrders: List<PartOrder>): Double {
        val positions = partOrders
            .map { partOrder ->
                partOrder.positions
            }
            .flatten()
            .toCollection(ArrayList())

        return product.parts.sumOf { part ->
            positions
                .find { position -> position.partSupplierId == part.partId }?.condition?.price?.toDouble()
                ?.times(part.count)
                ?: 0.0
        }
    }

    /**
     * Counts the number of currently open orders
     *
     * @return Number of open orders
     */
    fun countOpenOrders(productOrders: List<ProductOrder>): Number {
        return productOrders.count { productOrder ->
            productOrder.status === Status.OPEN
        }
    }

    /**
     * Calculates the productivity of the factory by counting the received and completed products
     *
     * @return Productivity = completed products / received products
     */
    fun calculateFactoryProductivity(productOrders: List<ProductOrder>): Number {
        var completedLastDay = productOrders
            .asSequence()
            .map { productOrder -> productOrder.products }
            .flatten()
            .toCollection(ArrayList())
            .filter { product ->
                timeService.getVirtualLocalTime(product.completionTime).toEpochMilli() ==
                    timeService.getVirtualLocalTime(System.currentTimeMillis())
                        .minus(24, ChronoUnit.HOURS)
                        .toEpochMilli()
            }.count()

        var receivedLastDay = productOrders
            .filter { productOrder ->
                timeService.getVirtualLocalTime(productOrder.receptionTime).toEpochMilli() ==
                    timeService.getVirtualLocalTime(System.currentTimeMillis())
                        .minus(24, ChronoUnit.HOURS)
                        .toEpochMilli()
            }
            .count()

        return if(receivedLastDay > 0) completedLastDay / receivedLastDay else completedLastDay
    }

    /**
     * Checks which products have been completed and pairs them with the
     * PartOrders from their respective ProductOrder to be able to calculate the products cost afterwards
     *
     * @return List of Pairs of Products and PartOrders
     */
    fun getCompletedProductPartOrdersPairs(productOrders: List<ProductOrder>): List<Pair<Product, List<PartOrder>>> {
        return productOrders
            .asSequence()
            .map { productOrder -> productOrder.products.map { product -> Pair(product, productOrder.partOrders) } }
            .flatten()
            .toCollection(ArrayList())
            .filter { productPartOrderPair -> productPartOrderPair.first.status == Status.DONE }
    }
}