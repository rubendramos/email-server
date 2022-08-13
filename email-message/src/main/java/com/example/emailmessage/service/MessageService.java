package com.example.emailmessage.service;

import java.util.Set;

import com.example.emailmessage.entity.Message;
import com.example.emailmessage.exceptions.EmailMessageServiceException;

public interface MessageService {

	/**
	 * Retrieves a list of ALL {@link Message}
	 * @return
	 */
	public Set<Message> listAllMessages();
	
	/**
	 * Gets {@link Message} by id
	 * @param id
	 * @return
	 */
	public Message getMessageById(Long id);
	
	/**
	 * Save  {@link Message} 
	 * @param message 
	 * @return Message
	 */
	public Message saveMessage(Message message) throws EmailMessageServiceException;	
	/**
	 * Update {@link Message}
	 * @param mail
	 * @return
	 */
	public Message updateMessage(Message message) throws EmailMessageServiceException;
	
	/**
	 * Delete {@link Message} by Id
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
