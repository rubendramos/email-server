package com.example.emailbox.service;

import java.util.Set;

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.EmailAddressKey;
import com.example.emailbox.entity.InBox;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.enums.StatusEnum;

public interface InBoxService {

	
	/**
	 * Retrieves an {@link Email} {@link Set} by string email address and {@link StatusEnum}
	 * @return
	 */
	public Set<Email> listEmailsFromAddressAndStatus(String addresParam, StatusEnum status) throws MailServiceException;
	
	/**
	 * Retrieves a {@link Email} {@link Set} by string email address
	 * @return
	 */
	public Set<Email> listEmailsFromAddress(String addressParam) throws MailServiceException;
	
	/**
	 * Retrieves a {@link Email}  {@link Set} by mesageId
	 * @param messageId
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> emailListByMessageId(Long messageId) throws MailServiceException;
	
	/**
	 * Send a {@link Email} by messageId
	 * @param mailId
	 * @return
	 * @throws MailServiceException
	 * @throws EmailStatusException
	 */
	public Email sendMail(Long messageId) throws MailServiceException,EmailStatusException;
	
	/**
	 * Delete an {@link Email} {@link InBox} by messageId and addressId
	 * @param messageId
	 * @param address
	 * @return
	 * @throws MailServiceException
	 */
	public Email deleteInBox(Long messageId,Long address) throws MailServiceException;
	
	/**
	 * Delete an {@link Email} {@link Set}   by messageId and addressId
	 * @param messageId
	 * @param addressId
	 * @return
	 * @throws MailServiceException
	 */
	public Set<Email> deleteInBox(Set<Long> messageId, Long addressId) throws MailServiceException;
	
	
	/**
	 * Updates {@link Email}  status by messageId and addressId
	 * @param messageId
	 * @param addressId
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailStatus(Long messageId, Long addressId , StatusEnum status) throws MailServiceException;
		
	
	/**
	 * Get {@link InBox} by Id
	 */
	public Email getInBoxMailById(EmailAddressKey emailAddressKey) throws MailServiceException;
}
