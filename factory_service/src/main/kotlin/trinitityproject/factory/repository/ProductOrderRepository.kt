package trinitityproject.factory.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import trinitityproject.factory.model.ProductOrder
import java.util.*
@Repository
interface ProductOrderRepository : ReactiveMongoRepository<ProductOrder, UUID>

