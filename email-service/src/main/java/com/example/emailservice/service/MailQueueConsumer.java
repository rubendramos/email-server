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
public class MailQueueConsumer {

	private final MailSender mailSender;

	private final String mailQueue;

	private final Receiver receiver;

	private final ObjectMapper mapper;
	
	Logger logger = LoggerFactory.getLogger(MailQueueConsumer.class);

	public MailQueueConsumer(MailSender mailSender, @Value("${mail.queue}") String mailQueue, Receiver receiver,
			ObjectMapper mapper) {
		this.mailSender = mailSender;
		this.mailQueue = mailQueue;
		this.receiver = receiver;
		this.mapper = mapper;
	}

	@PostConstruct
	public void startConsume() {
		this.receiver.consumeAutoAck(this.mailQueue).subscribe(message -> {
			try {
				val mail = this.mapper.readValue(new String(message.getBody()), Mail.class);
				this.mailSender.send(mail).subscribe(data -> {
					logger.info("Mail sent successfully");
				});
			} catch (IOException e) {
				throw new RuntimeException("Error object deserialization ");
			} catch (Exception e) {
				throw new RuntimeException("Error sending mail");
			}
		});
	}

}
