package com.example.emailbox.service;

import java.util.Set;

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.InBox;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.modelo.enums.StatusEnum;

public interface InBoxService {

	
	/**
	 * Retrieves a list o emails by address and {@link StatusEnum}
	 * @return
	 */
	public Set<Email> listEmailsFromAddresAndStatus(String addresParam, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves a Set of emilas by address
	 * @return
	 */
	public Set<Email> listEmailsFromAddres(String addresParam) throws MailServiceException;
	
	
	/**
	 * Sends a mail
	 * @param mailId
	 * @return
	 * @throws MailServiceException
	 * @throws EmailStatusException
	 */
	public Email sendMail(Long mailId) throws MailServiceException,EmailStatusException;
	
	/**
	 * Delete a mail in InBox by id and addressId
	 * @param id
	 * @param address
	 * @return
	 * @throws MailServiceException
	 */
	public InBox deleteInBox(Long id,Long address) throws MailServiceException;
	

	/**
	 * Delete a set of mails by   id's and address
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> deleteInBox(Set<Long> mailsIds, Long address) throws MailServiceException;
	
	
	/**
	 * Updates mail status by mailId and addressId
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailStatus(Long mailId, Long addressId , StatusEnum status) throws MailServiceException;
		
	
	
	/**
	 * Marca como spam todo los mails enviados desde una dirección 
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> setInBoxMailsAsSpam(String addressParam) throws MailServiceException;
}
