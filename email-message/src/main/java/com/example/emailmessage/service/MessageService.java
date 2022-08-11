package com.example.emailmessage.service;

import java.util.Set;

import com.example.emailmessage.entity.Message;
import com.example.emailmessage.exceptions.EmailMessageServiceException;

public interface MessageService {

	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Message> listAllMessages();
	
	/**
	 * Gets mail by id
	 * @param id
	 * @return
	 */
	public Message getMessageById(Long id);
	
	/**
	 * Create a mail
	 * @param mail
	 * @return
	 */
	public Message saveMessage(Message message) throws EmailMessageServiceException;	
	/**
	 * Update a mail
	 * @param mail
	 * @return
	 */
	public Message updateMessage(Message message) throws EmailMessageServiceException;
	
	/**
	 * Delete a mail
	 * @param id
	 * @return
	 */
	public Message deleteMessage(Long id);
	
	
	/**
	 * Delete multiple mails
	 * @param id
	 * @return
	 */
	public Set<Message> deleteMessage(Set<Long> id);
	

	
	
	
	
}
