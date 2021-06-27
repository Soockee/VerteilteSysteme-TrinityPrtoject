package trinitityproject.factory.service

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import trinitityproject.factory.model.condition.Condition
import trinitityproject.factory.model.condition.ConditionRequest
import trinitityproject.factory.model.condition.PartCondition
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
class ConditionService(
    private val conditionCache: ConcurrentMap<UUID, PartCondition>,
    private val template: RabbitTemplate,
) {
    @Bean
    fun conditionCache(): ConcurrentMap<UUID, PartCondition> {
        return ConcurrentHashMap()
    }

    fun getCondition(partId: UUID): PartCondition? {
        return if (conditionCache.containsKey(partId)) {
            conditionCache[partId]
        } else {
            val newCondition = template.convertSendAndReceiveAsType(
                "conditionRequests",
                ConditionRequest(partId),
                object : ParameterizedTypeReference<PartCondition>() {}
            )
            conditionCache[partId] = newCondition
            newCondition
        }
    }

    fun getBestCondition(partId: UUID): Condition {
        var partCondition = getCondition(partId)
        while (partCondition == null) {
            partCondition = getCondition(partId)
        }
        return partCondition
            .conditions.minByOrNull {
                it.currency
            }!!
    }

}