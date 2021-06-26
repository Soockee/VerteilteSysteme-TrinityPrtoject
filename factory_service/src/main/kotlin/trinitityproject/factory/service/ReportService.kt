package trinitityproject.factory.service

import org.springframework.stereotype.Service
import trinitityproject.factory.model.Product
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.model.Report
import trinitityproject.factory.model.Status
import trinitityproject.factory.repository.ProductOrderRepository
import java.time.Duration
import java.util.*

@Service
class ReportService(
    private val repository: ProductOrderRepository,
    private val timeService: TimeService
) {
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

        val productOrdersToday = this.getProductOrdersToday(productOrders);

        val finishedProducts: List<Product> = this.getFinishedProductsToday(productOrders);

        val report = Report(
            productOrdersToday.size,
            finishedProducts.size,
            this.calculateCosts(finishedProducts));

        return report;
    }

    /**
     * Calculates the cost of finished goods (cost of parts + production time)
     *
     * @return Cost of finished goods
     */
    fun calculateCosts(products: List<Product>): Number {
        var partCosts: Double = 0.0
        var productionTime: Double = 0.0

        products.forEach { product ->
            // TODO(Fabian): Get Part costs from part conditions

            productionTime += product.productionTime
        }

        return partCosts + productionTime * this.costPerHour;
    }

    /**
     * Gets the orders received since midnight
     *
     * @return Orders received since midnight
     */
    fun getProductOrdersToday(productOrders: List<ProductOrder>): List<ProductOrder> {
        return productOrders.filter { productOrder -> timeService.isToday(productOrder.receptionTime) }
    }

    /**
     * Gets the finished goods since midnight
     *
     * @return Finished goods since midnight
     */
    fun getFinishedProductsToday(productOrders: List<ProductOrder>): List<Product> {
        var finishedProducts: MutableList<Product> = ArrayList()

        productOrders.forEach { productOrder ->
            productOrder.products.forEach { product ->
                if(product.status == Status.DONE && this.timeService.isToday(product.completionTime)) {
                    finishedProducts.add(product)
                }
            }
        }

        return finishedProducts
    }
}