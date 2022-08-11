package com.example.emailmessage.service;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.emailmessage.entity.Message;
import com.example.emailmessage.exceptions.EmailMessageServiceException;
import com.example.emailmessage.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
		
	
	Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	

	private final MessageRepository messageRepository;

	@Override
	public Set<Message> listAllMessages() {
		return new HashSet<>(messageRepository.findAll());
	}

	@Override
	public Message getMessageById(Long id) {
		return messageRepository.findById(id).orElse(null);
	}
	
	
	
	@Override
	@Transactional
	public Message saveMessage(Message message) throws EmailMessageServiceException {
		message.setCreateAt(new Date());
		return messageRepository.save(message);
	}

	@Override
	@Transactional
	public Message updateMessage(Message message) throws EmailMessageServiceException {
		Message dbMessage = this.getMessageById(message.getId());

		dbMessage.setEmailBody(message.getEmailBody());
		dbMessage.setEmailCc(message.getEmailCc());
		dbMessage.setEmailTo(message.getEmailTo());
		dbMessage.setUpdateAt(new Date());
		return messageRepository.save(dbMessage);
	}	
	
	
	
	@Override
	public Set<Message> deleteMessage(Set<Long> messagesIds) {
		Set<Message> deletedMessagesList = new HashSet<>();
		messagesIds.forEach(messageId ->{
			Message deletedMessage = deleteMessage(messageId);
			if(null != deletedMessage) {
				deletedMessagesList.add(deletedMessage);
			}
		});
		
		return deletedMessagesList;
	}
	
	
	@Override
	public Message deleteMessage(Long id) {
		Message dbMessage = this.getMessageById(id);
		if (null != dbMessage) {
			messageRepository.delete(dbMessage);
		} 
		
		return dbMessage;
	}
	

}
