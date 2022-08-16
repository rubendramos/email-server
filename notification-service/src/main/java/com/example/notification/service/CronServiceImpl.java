package com.example.notification.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CronServiceImpl implements CronService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationSenderServiceImpl.class);

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Value("${spam.queue}")
	private String queue;

	@Override
	public void updateToSpan(Set<String> addresEmailList) {

		addresEmailList.forEach(addressEmail -> {
			try {
				
				logger.info("Updating email address as spam: " + addressEmail);
				amqpTemplate.convertAndSend(this.queue, addressEmail);
				logger.info("Updated email addres as spam  = " + addressEmail);
				
			} catch (AmqpException e) {
				logger.error("Error sending message to queue ", e);
			} catch (Exception e) {
				logger.error("Error updating to spam", e);
			}

		});

	}

}
