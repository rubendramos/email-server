package com.example.emailmessage.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailmessage.entity.Message;
import com.example.emailmessage.exceptions.EmailMessageServiceException;
import com.example.emailmessage.service.MessageService;

@RestController
@RequestMapping(value = "/api/message")
public class EmailMessageController {

	Logger logger = LoggerFactory.getLogger(EmailMessageController.class);

	@Autowired
	private MessageService messageService;

	
	/**
	 * Retrieves {@link Message} by Id
	 * @param messageId
	 * @return
	 */
	@GetMapping(value = "message/{messageId}")
	public ResponseEntity<Message> getMessageById(@PathVariable("messageId") Long messageId) {
		Message message = messageService.getMessageById(messageId);
		if (message != null) {
			return ResponseEntity.ok(message);
		}
		return ResponseEntity.noContent().build();

	}
	
	
	/**
	 * Retrieve all messages
	 * @return
	 */
	@GetMapping("/allMessages")
	public ResponseEntity<Set<Message>> listMails() {
		Set<Message> messageList = messageService.listAllMessages();
		if (!messageList.isEmpty()) {
			return ResponseEntity.ok(messageList);
		}
		return ResponseEntity.noContent().build();

	}
	
	
	/**
	 * Persist  {@link Message}
	 * @param message
	 * @return
	 * @throws EmailMessageServiceException
	 */
	@PostMapping
	public ResponseEntity<Message> saveMessage(@RequestBody Message message)
			throws EmailMessageServiceException {
		Message messaUpdated = null;

		try {
			
			messaUpdated = messageService.saveMessage(message);
			if (messaUpdated == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			throw new EmailMessageServiceException(e, new Object[] { message });
		}

		return ResponseEntity.ok(messaUpdated);
	}

	/**
	 * Updates {@link Message} by Id
	 * @param messageId
	 * @param message
	 * @return
	 * @throws EmailMessageServiceException
	 */
	@PutMapping(value = "update/{messageId}")
	public ResponseEntity<Message> updateMessage(@PathVariable("messageId") Long messageId, @RequestBody Message message)
			throws EmailMessageServiceException {
		Message messaUpdated = null;

		try {
			message.setId(messageId);
			messaUpdated = messageService.updateMessage(message);
			if (messaUpdated == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			throw new EmailMessageServiceException(e, new Object[] { messageId });
		}

		return ResponseEntity.ok(messaUpdated);
	}

}
