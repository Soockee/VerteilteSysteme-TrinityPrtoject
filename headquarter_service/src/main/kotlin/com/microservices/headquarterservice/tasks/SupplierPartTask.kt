package com.microservices.headquarterservice.tasks

import com.microservices.headquarterservice.model.supplier.SupplierOrder
import com.microservices.headquarterservice.persistence.PartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderPartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupplierPartTask(
    private val orderRepository: SupplierOrderRepository,
    private val orderPartRepository: SupplierOrderPartRepository,
    private val partRepository: PartRepository,
) {

    fun scheduleTaskWithFixedDelay(supplierOrder: SupplierOrder) {
//        orderRepository.findById(supplierOrder.supplier_id)
//            .flatMap { order -> orderPartRepository.findAllById(order.supplier_id)}
//        Timer().schedule(updateStatusTask(supplierOrder), )
    }

    fun updateStatusTask(supplierOrder: SupplierOrder): TimerTask {
        return SupplierPartTimerTask(supplierOrder,orderRepository,orderPartRepository)
    }


    class SupplierPartTimerTask(
        private val supplierOrder: SupplierOrder,
        private val orderRepository: SupplierOrderRepository,
        private val orderPartRepository: SupplierOrderPartRepository,
    ) : TimerTask() {
        override fun run() {
            supplierOrder.status = "complete"
            orderRepository.save(supplierOrder)
        }
    }
}