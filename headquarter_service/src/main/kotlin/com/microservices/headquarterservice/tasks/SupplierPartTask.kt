package com.microservices.headquarterservice.tasks

import com.microservices.headquarterservice.model.common.Part
import com.microservices.headquarterservice.model.headquarter.ProductPart
import com.microservices.headquarterservice.model.headquarter.order.OrderProductResponse
import com.microservices.headquarterservice.model.supplier.SupplierOrder
import com.microservices.headquarterservice.persistence.OrderRepository
import com.microservices.headquarterservice.persistence.PartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderPartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderRepository
import com.microservices.headquarterservice.service.ConditionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupplierPartTask(
    private val supplierOrderRepository: SupplierOrderRepository,
    private val partRepository: PartRepository,
    private val supplierOrderPartRepository: SupplierOrderPartRepository,
) {
    companion object {
        val logger = LoggerFactory.getLogger(ConditionService::class.java)
    }

    fun scheduleTaskWithDelay(supplierOrder: SupplierOrder) {
        val supplierOrderParts = supplierOrderPartRepository.findAll().collectList().block()!!
        val parts = partRepository.findAll().collectList().block()!!
        var done = false
        for (supplierOrderPart in supplierOrderParts.filter { supplierOrderPart -> supplierOrderPart.supplier_order_id == supplierOrder.order_id }) {
            for (part in parts.filter {  e -> e.part_id == supplierOrderPart.part_id  }){
                Timer().schedule(updateStatusTask(supplierOrder), part.delievery_time.times(1000).toLong())
                logger.warn("SupplierOrder:  ${supplierOrder.order_id}  is  Scheduled for ${part.delievery_time} seconds")
                done = true
                break;
            }
            if(done)break;
        }
    }

    fun updateStatusTask(supplierOrder: SupplierOrder): TimerTask {
        return SupplierPartTimerTask(supplierOrder, supplierOrderRepository)
    }


    class SupplierPartTimerTask(
        private val supplierOrder: SupplierOrder,
        private val orderRepository: SupplierOrderRepository,
    ) : TimerTask() {
        override fun run() {
            supplierOrder.status = "complete"
            val supplierOrderSaved = orderRepository.save(supplierOrder).block()!!
            logger.warn("SupplierOrder:  ${supplierOrderSaved.order_id}  is  ${supplierOrderSaved.status}")
        }
    }
}