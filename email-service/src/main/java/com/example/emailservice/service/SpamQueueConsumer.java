package com.example.emailservice.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.emailservice.domain.Mail;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import reactor.rabbitmq.Receiver;

/**
 * Defines the 
 * @author rdr
 * @Since 8 ago 2022
 * @Version 1.0
 */

@Service
public class SpamQueueConsumer {

	private final SpamUpdater spamUpdater;

	private final String spamQueue;

	private final Receiver receiver;
	
	private final ObjectMapper mapper;

	
	Logger logger = LoggerFactory.getLogger(SpamQueueConsumer.class);

	public SpamQueueConsumer(SpamUpdater spamUpdater, @Value("${spam.queue}") String mailQueue, Receiver receiver,ObjectMapper mapper) {
		this.spamUpdater = spamUpdater;
		this.spamQueue = mailQueue;
		this.receiver = receiver;
		this.mapper = mapper;
	}

	@PostConstruct
	public void startConsume() {
		this.receiver.consumeAutoAck(this.spamQueue).subscribe(message -> {
			try {
				val mailAddress = this.mapper.readValue(new String(message.getBody()), String.class);
				this.spamUpdater.updateSpam(mailAddress).subscribe(data -> {
					logger.info("Update Spam successfully");
				});
			} catch (Exception e) {
				throw new RuntimeException("Error updating to spam");
			}
		});
	}

}
