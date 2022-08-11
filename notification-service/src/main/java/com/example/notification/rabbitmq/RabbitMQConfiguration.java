package com.example.notification.rabbitmq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Set RabbitMQ Configuration
 * 
 * @author rdr
 * @Since 8 ago 2022
 * @Version 1.0
 */

@Configuration
public class RabbitMQConfiguration {

	private final String pass;

	private final String user;

	private final String host;

	private final Integer port;

	
	public RabbitMQConfiguration(@Value("${notification.rabbitmq.password}") String pass,
			@Value("${notification.rabbitmq.username}") String user, @Value("${notification.rabbitmq.host}") String host,
			@Value("${notification.rabbitmq.port}") Integer port) {
		this.pass = pass;
		this.user = user;
		this.host = host;
		this.port = port;
		
	}

	@Bean("notificationRabbitmqConnectionFactory")
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setUsername(this.user);
		factory.setPassword(this.pass);
		factory.setHost(this.host);
		factory.setPort(this.port);
		return factory;
	}

	@Bean
	AmqpAdmin amqpAdmin(@Qualifier("notificationRabbitmqConnectionFactory") ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	


}
