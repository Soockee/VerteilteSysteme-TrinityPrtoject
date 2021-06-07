package trinitityproject.factory.messaging

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@ConfigurationProperties("destinations")
@Component
class DestinationsConfig {
    private var queues: Map<String, DestinationInfo> = HashMap()

    private var topics: Map<String, DestinationInfo> = HashMap()


    fun getQueues(): Map<String, DestinationInfo> {
        return queues
    }

    fun setQueues(queues: Map<String, DestinationInfo>) {
        this.queues = queues
    }

    fun getTopics(): Map<String, DestinationInfo> {
        return topics
    }

    fun setTopics(topics: Map<String, DestinationInfo>) {
        this.topics = topics
    }

    // DestinationInfo stores the Exchange name and routing key used
    // by our producers when posting messages
    class DestinationInfo {
        var exchange: String? = null
        var routingKey: String? = null
    }
}