package trinitityproject.factory.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import trinitityproject.factory.model.Part
import trinitityproject.factory.model.Product
import trinitityproject.factory.model.ProductData
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

internal class PartOrderServiceTest {

    @Mock
    private var partOrderService = PartOrderService(mock(ProductOrderRepository::class.java))

    @Test
    fun getRequiredParts() {
        runBlocking {
            val input = ProductOrder(
                customerId = UUID.randomUUID(),
                products = listOf(
                    Product(
                        productData = ProductData(
                            productId = UUID.randomUUID(),
                            productionTime = 5000,
                            name = "test"
                        ),
                        count = 4,
                        completionTime = 10000,
                        parts = listOf(
                            Part(
                                partId = UUID.fromString("d94e7dac-e97e-4b3d-9207-610e4eaa3f80"),
                                count = 2
                            ),
                            Part(
                                partId = UUID.fromString("273b659a-7fa7-4029-bf92-421fff457ddf"),
                                count = 1
                            )
                        )
                    ),
                    Product(
                        productData = ProductData(
                            productId = UUID.randomUUID(),
                            productionTime = 5000,
                            name = "test"
                        ),
                        count = 1,
                        completionTime = 10000,
                        parts = listOf(
                            Part(
                                partId = UUID.fromString("d94e7dac-e97e-4b3d-9207-610e4eaa3f80"),
                                count = 2
                            ),
                            Part(
                                partId = UUID.fromString("cf9ae254-c466-11eb-8529-0242ac130003"),
                                count = 1
                            )
                        )
                    )
                )
            )
            assertEquals(
                mapOf(
                    Pair(UUID.fromString("d94e7dac-e97e-4b3d-9207-610e4eaa3f80"), 10),
                    Pair(UUID.fromString("273b659a-7fa7-4029-bf92-421fff457ddf"), 4),
                    Pair(UUID.fromString("cf9ae254-c466-11eb-8529-0242ac130003"), 1)
                ),
                partOrderService.getRequiredParts(input)
            )
        }
    }
}