package com.example.emailbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.emailbox.modelo.Message;

@FeignClient(name = "email-message", path= "/api/message")
public interface MessageClient {

	
	/**
	 * Gets {@link Message} by Id
	 * @param messageId
	 * @return
	 */
	@GetMapping(value = "message/{messageId}")
	public ResponseEntity<Message> getMessageById(@PathVariable("messageId") Long messageId);
	
	/**
	 * Save {@link Message}  
	 * @param message
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Message> saveMessage(@RequestBody Message message);

	/**
	 * Updates {@link Message}
	 * @param messageId
	 * @param message
	 * @return
	 */
	@PutMapping(value = "update/{messageId}")
	public ResponseEntity<Message> updateMail(@PathVariable("messageId") Long messageId, @RequestBody Message message);
}
