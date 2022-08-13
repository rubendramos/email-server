package com.example.emailbox.service;

import java.util.Set;

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.enums.StatusEnum;

public interface OutBoxService {

	

	/**
	 * Retrieves an {@link OutBox} {@link Email} by Id
	 * @return
	 */
	public Email getOutBoxById(Long mailId);
	
	/**
	 * Retrieves a {@link OutBox} {@link Set} by string email address and {@link StatusEnum}
	 * @return
	 */
	public Set<Email> listEmailsFromAddresAndStatus(String address, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves a {@link OutBox} {@link Set} by string email address
	 * @return
	 */
	public Set<Email> listEmailsFromAddres(String address) throws MailServiceException;
	
	
	/**
	 * Save {@link OutBox} {@link Email}
	 * @param email
	 * @return
	 * @throws MailServiceException
	 */
	public Email createOutBox(Email email) throws MailServiceException;
	
	
	/**Delete {@link OutBox} {@link Email} by Id*/
	public Email deleteOutBox(Long emailId) throws MailServiceException;
	
	
	/**
	 * Updates {@link OutBox}  {@link StatusEnum}  by Id
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailStatus(Long mailId, StatusEnum status) throws MailServiceException;

	/**
	 * Delete a {@link Set}  of {@link OutBox}  by id {@link Set}
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> deleteOutBox(Set<Long> mailsIds) throws MailServiceException;
	
	/**
	 * Set as {@link StatusEnum} SPAN All {@link Email} sent from {@link Address}
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> setOutBoxMailsAsSpam(String addressParam) throws MailServiceException;
	
	
//	public Email updateMailMessage(Long mailId, Email email) throws MailServiceException{
	
}
