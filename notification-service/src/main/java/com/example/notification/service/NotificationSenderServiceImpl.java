package com.example.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.notification.domain.Mail;

@Service
@EnableRabbit
public class NotificationSenderServiceImpl implements NotificationSenderService{
	private static final Logger logger = LoggerFactory.getLogger(NotificationSenderServiceImpl.class);
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	@Value("${notification.queue}")
	private String queue;
	
	
	
	public void sendNotification(Mail mail) {	
		try {
		logger.info("Sendindg mail = " + mail);
		amqpTemplate.convertAndSend(this.queue, mail);
		logger.info("Sended mail = " + mail);
		}catch(AmqpException e){
			logger.error("Error sending notification to queue " , e);
		}catch(Exception e){
			logger.error("Error sending notification " , e);
		}
	}
	


}
