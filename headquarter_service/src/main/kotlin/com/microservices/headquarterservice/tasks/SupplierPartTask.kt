package com.microservices.headquarterservice.tasks

import com.microservices.headquarterservice.model.common.Part
import com.microservices.headquarterservice.model.supplier.SupplierOrder
import com.microservices.headquarterservice.persistence.PartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderPartRepository
import com.microservices.headquarterservice.persistence.SupplierOrderRepository
import com.microservices.headquarterservice.service.ConditionService
import com.microservices.headquarterservice.service.TimeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit


@Component
class SupplierPartTask(
    private val supplierOrderRepository: SupplierOrderRepository,
    private val partRepository: PartRepository,
    private val supplierOrderPartRepository: SupplierOrderPartRepository,
    private val timeService: TimeService
) {
    companion object {
        val logger = LoggerFactory.getLogger(SupplierPartTask::class.java)
    }

    fun scheduleTaskWithDelay(supplierOrder: SupplierOrder) {
        val supplierOrderParts = supplierOrderPartRepository.findAll().collectList().block()!!
        val parts = partRepository.findAll().collectList().block()!!
        val partTimerList  = mutableListOf<FutureTask<*>>()
        var minScheduleTime = Int.MIN_VALUE
        for (supplierOrderPart in supplierOrderParts.filter { supplierOrderPart -> supplierOrderPart.supplier_order_id == supplierOrder.order_id }) {
            for (part in parts.filter {  e -> e.part_id == supplierOrderPart.part_id  }){
                val deliveryTimeMillis = part.delievery_time * 1000
                val future = updateStatusTask(part,SupplierPartTimerTask(part), timeService.realtimeToVirtualTimeMillis(deliveryTimeMillis.toLong()))
                partTimerList.add(future)
                if(part.delievery_time > minScheduleTime){
                    minScheduleTime = part.delievery_time
                }
            }
        }
        logger.info("SupplierOrder:  ${supplierOrder.order_id}  is  Scheduled for ${minScheduleTime} seconds")
        val checkAllPartsTimer = checkAllSupplierPartTask(partTimerList,supplierOrder)
        Timer().schedule(checkAllPartsTimer,0)
    }

    fun updateStatusTask(part: Part,timer: TimerTask, delay: Long): FutureTask<*> {
        val futureTask: FutureTask<*> = FutureTask<Void?>(SupplierPartTimerTask(part), null)
        val scheduler = Executors.newScheduledThreadPool(1)
        scheduler.schedule(futureTask, delay, TimeUnit.MILLISECONDS)
        logger.info("SupplierOrderPart:  ${part.part_id}  is  Scheduled for ${part.delievery_time} seconds")

        return futureTask
    }

    fun checkAllSupplierPartTask(partTimerList: List<FutureTask<*>>, supplierOrder: SupplierOrder): TimerTask {
        return SupplierPartListCheck(partTimerList,supplierOrder, supplierOrderRepository)
    }


    class SupplierPartTimerTask(
        private val part: Part,
    ) : TimerTask() {
        override fun run() {
            logger.warn("part:  ${part.part_id} with name ${part.name} is complete and took  ${part.delievery_time} seconds")
        }
    }
    class SupplierPartListCheck(
        private val partTimerList :List<FutureTask<*>>,
        private val supplierOrder: SupplierOrder,
        private val orderRepository: SupplierOrderRepository,
    ) : TimerTask() {
        override fun run() {
            for (timer in partTimerList){
                while(!timer.isDone);
            }
            supplierOrder.status = "complete"
            val supplierOrderSaved = orderRepository.save(supplierOrder).block()!!
            logger.warn("SupplierOrder:  ${supplierOrderSaved.order_id}  is  ${supplierOrderSaved.status}")
        }
    }
}