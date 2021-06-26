package trinitityproject.factory.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import trinitityproject.factory.model.Part
import trinitityproject.factory.model.Product
import trinitityproject.factory.model.ProductOrder
import trinitityproject.factory.repository.ProductOrderRepository
import java.util.*

internal class PartOrderServiceTest {

    @Mock
    private var partOrderService = PartOrderService(mock(ProductOrderRepository::class.java))

    @Test
    fun getRequiredParts() {
        val input = ProductOrder(
            customerId = UUID.randomUUID(),
            products = listOf(
                Product(
                    productId = UUID.randomUUID(),
                    count = 4,
                    productionTime = 5000,
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
                    productId = UUID.randomUUID(),
                    count = 1,
                    productionTime = 5000,
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