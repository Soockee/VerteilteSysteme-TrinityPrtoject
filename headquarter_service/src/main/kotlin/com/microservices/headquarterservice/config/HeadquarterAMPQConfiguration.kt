package com.microservices.headquarterservice.service

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class HeadquarterAMPQConfiguration {

	@Value("${microservice.rabbitmq.queue}")
	val headquarterQueueName: String? = null

	@Value("${microservice.rabbitmq.exchange}")
	val headquarterExchangeName: String? = null
	
	@Value("${microservice.rabbitmq.routingkey}")
	val headquarterRoutingKey: String? = null



	@Bean
	fun queue(): Queue  {
		return Queue(headquarterQueueName, false);
	}

	@Bean
	fun exchange(): DirectExchange  {
		return DirectExchange(headquarterExchangeName);
	}

	@Bean
	fun binding(queue: Queue, exchange: DirectExchange ): Binding  {
		return BindingBuilder.bind(queue).to(exchange).with(headquarterRoutingKey);
	}

	@Bean
	fun jsonMessageConverter(): MessageConverter  {
		return Jackson2JsonMessageConverter();
	}

	
	@Bean
	fun rabbitTemplate(connectionFactory: ConnectionFactory ): AmqpTemplate  {
		val rabbitTemplate: RabbitTemplate  =  RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}