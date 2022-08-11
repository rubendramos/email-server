package com.example.emailbox.service;

import java.util.Set;

import com.example.emailbox.entity.Email;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.modelo.enums.StatusEnum;

public interface OutBoxService {

	
	/**
	 * Retrieves all mails list
	 * @return
	 */
//	public Set<Message> listEmailsFromAddresAndStatus(String address, StatusEnum status) throws MailServiceException;
//	
//	/**
//	 * Retrieves all mails list
//	 * @return
//	 */
//	public Set<Message> listEmailsFromAddres(String address) throws MailServiceException;
	
	
	
	/**
	 * Retrieves a outBox mail by Id
	 * @return
	 */
	public Email getOutBoxById(Long mailId);
	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Email> listEmailsFromAddresAndStatus(String address, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves all mails list
	 * @return
	 */
	public Set<Email> listEmailsFromAddres(String address) throws MailServiceException;
	
	
	/**Sabe a outbox mail*/
	public Email createOutBox(Email email) throws MailServiceException;
	
	
	/**Delete a outbox mail*/
	public Email deleteOutBox(Long emailId) throws MailServiceException;
	
	
	/**
	 * Updates mail status by mailId and addressId
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailStatus(Long mailId, StatusEnum status) throws MailServiceException;

	/**
	 * Delete a set of mails by id
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> deleteOutBox(Set<Long> mailsIds) throws MailServiceException;
	
	/**
	 * Marca como spam todo los mails enviados desde una dirección 
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> setOutBoxMailsAsSpam(String addressParam) throws MailServiceException;
	
	
}
