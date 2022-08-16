package com.example.emailbox.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.emailbox.modelo.Message;

@Component
public class MessageHystrixFallBackFactory implements MessageClient {

	
	private static final String UNKNOWN_STRING = "Unknown";

	@Override
	public ResponseEntity<Message> getMessageById(Long messageId) {
		return  ResponseEntity.ok(getUnknownMessage());
	}

	@Override
	public ResponseEntity<Message> saveMessage(Message message) {
		// DO nothing
		return null;
	}

	@Override
	public ResponseEntity<Message> updateMail(Long messageId, Message message) {
		// DO nothing
		return  null;
	}
	
	/**
	 * Retrieves a unknown address
	 * @return
	 */
	private Message getUnknownMessage() {
		return  Message.builder()
				.emailBody(UNKNOWN_STRING)
				.emailCc(UNKNOWN_STRING)
				.emailFrom(UNKNOWN_STRING)
				.emailTo(UNKNOWN_STRING).build();
	}


}
