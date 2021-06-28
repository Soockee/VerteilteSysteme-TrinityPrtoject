package trinitityproject.factory.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import trinitityproject.factory.model.condition.PartCondition
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Configuration
class ConditionServiceConfig {

    @Bean
    fun conditionCache(): ConcurrentMap<UUID, PartCondition> {
        return ConcurrentHashMap()
    }

}