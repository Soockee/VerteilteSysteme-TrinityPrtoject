package trinitityproject.factory.messaging

import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.stereotype.Component


@Component
class MessageListenerContainerFactory(private val connectionFactory: ConnectionFactory) {

    fun createMessageListenerContainer(queueName: String?): MessageListenerContainer? {
        val mlc = SimpleMessageListenerContainer(connectionFactory)
        mlc.addQueueNames(queueName)
        return mlc
    }
}