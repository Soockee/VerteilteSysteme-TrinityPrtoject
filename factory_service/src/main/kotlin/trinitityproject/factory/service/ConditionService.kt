package trinitityproject.factory.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.reactive.asFlow
import org.springframework.amqp.AmqpException
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toMono
import trinitityproject.factory.model.condition.Condition
import trinitityproject.factory.model.condition.ConditionRequest
import trinitityproject.factory.model.condition.PartCondition
import java.util.*
import java.util.concurrent.ConcurrentMap


@Component
class ConditionService(
    private val conditionCache: ConcurrentMap<UUID, PartCondition>,
    private val template: RabbitTemplate,
) {

    /**
     * Returns a collection of Conditions for the given partId.
     * If a condition for a part is not cached a PartCondition is requested over a rabbitmq-rpc-call to the Headquarter.
     *
     * @param partId the ID of the Part
     *
     * @return A collection of Conditions for the given Part
     */
    suspend fun getCondition(partId: UUID): PartCondition? {
        return if (conditionCache.containsKey(partId)) {
            conditionCache[partId]
        } else {
            val newCondition = template.convertSendAndReceiveAsType(
                "condition",
                ConditionRequest(partId),
                object : ParameterizedTypeReference<PartCondition>() {}
            )
            conditionCache[partId] = newCondition // A received condition will be cached
            newCondition
        }
    }


    /**
     * Returns condition with the lowest price for the part
     *
     * @param partId The ID of the Part
     *
     *
     */
    suspend fun getBestCondition(partId: UUID): Condition {
        val partCondition =
            getCondition(partId)
                .toMono()
                .asFlow()
                .retry(5) { exception ->
                    (exception is AmqpException).also { if (it) delay(1000) }
                }
                .filterNotNull()
                .first()

        return partCondition
            .conditions
            .minByOrNull {
                it.currency
            }!!
    }

}