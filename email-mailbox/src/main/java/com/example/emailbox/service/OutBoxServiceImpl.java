package com.example.emailbox.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.emailbox.client.AddressClient;
import com.example.emailbox.client.MessageClient;
import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.exceptions.NoAddressDomainException;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.repository.OutBoxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutBoxServiceImpl implements OutBoxService {

	Logger logger = LoggerFactory.getLogger(OutBoxServiceImpl.class);
	private final OutBoxRepository outBoxRepository;
	
	@Autowired
	AddressClient addressClient;
	
	@Autowired
	MessageClient mesageClient;
	
	
	
	/**
	 * Retrieves a outBox mail by Id
	 * @return
	 */
	@Override
	public Email getOutBoxById(Long mailId) {
		return outBoxRepository.findById(mailId).get();
	}
	
	
	@Override
	public Set<Email> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {
		Set<Email> outBoxList = null;
		
		Address address = getAddress(stringAddress);
		if(address != null) {
			outBoxList = outBoxRepository.findByAddressAndEmailStatusValue(address, status.getStatusId());
		}
		
		return outBoxList;
	}

	@Override
	public Set<Email> listEmailsFromAddres(String stringAddress) throws MailServiceException {
		Set<Email> outBoxList = null;
		Address address = getValidatedAddress(stringAddress);
		if(address != null) {
			outBoxList = outBoxRepository.findByAddress(address);		
		}
		
		return outBoxList;

	}
	
	
	@Override
	public Email createOutBox(Email email) throws MailServiceException {
		Email savedEmail = null;
		Address address = getValidatedAddress(email.getAddress().getAddress());
		if(address != null) {
			email.getMessage().setCreateAt(new Date());
			email.getMessage().setUpdateAt(null);
			email.setAddress(address);
			email.setEmailStatusValue(StatusEnum.BORRADOR.getStatusId());
			email.setEmailStatus(StatusEnum.BORRADOR);
			savedEmail = saveOutBoxEmail((OutBox)email);
		}
				
		return  savedEmail;
	}
	
	
	@Override
	public Email deleteOutBox(Long id){
		Email ematilToDeleted = null;
		ematilToDeleted = outBoxRepository.getById(id);
		if (null != ematilToDeleted && ematilToDeleted.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {
			
			ematilToDeleted.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			outBoxRepository.save((OutBox)ematilToDeleted);
		} else {
			ematilToDeleted = null;
		}
		
		return ematilToDeleted;
	}
	
	
	@Override
	public Set<Email> deleteOutBox(Set<Long> mailsIds) throws MailServiceException{
		Set<Email> deletedMails = new HashSet<>();
		
		mailsIds.forEach(mailId ->{
			Email deleteMail = deleteOutBox(mailId);
			if(null != deleteMail) {
				deletedMails.add(deleteMail);
			}
		});
		
		return deletedMails;
	}
	
	/**
	 * Update status from inBox email
	 * @param mailId
	 * @param addressId
	 * @param status
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailStatus(Long mailId, StatusEnum status) throws MailServiceException{
		OutBox emaiToBeUpdated = null;
		emaiToBeUpdated = outBoxRepository.findById(mailId).get();
		
		if (null != emaiToBeUpdated && emaiToBeUpdated.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {
			emaiToBeUpdated.setEmailStatusValue(status.getStatusId());
			outBoxRepository.save(emaiToBeUpdated);
		} else {
			emaiToBeUpdated = null;
		}
		
		return emaiToBeUpdated;
		
	}
	
	
	/**
	 * Update status from inBox email
	 * @param mailId
	 * @param addressId
	 * @param status
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailMessage(Long mailId, Email email) throws MailServiceException{
		OutBox emaiToBeUpdated = null;
		emaiToBeUpdated = outBoxRepository.findById(mailId).get();
		
		if (null != emaiToBeUpdated && emaiToBeUpdated.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {
			emaiToBeUpdated.setMessage(email.getMessage());
			outBoxRepository.save(emaiToBeUpdated);
		} else {
			emaiToBeUpdated = null;
		}
		
		return emaiToBeUpdated;
	}
	
	
	/**
	 * Marca como spam todo los mails enviados desde una dirección 
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	@Transactional
	public Set<Email> setOutBoxMailsAsSpam(String stringAddress) throws MailServiceException{
		Set<Email> inBoxList = null; 
		
		try {
		
			Address address = getAddress(stringAddress);
			if(address != null) {
				inBoxList = outBoxRepository.findByAddressAndEmailStatusValue(address,StatusEnum.ENVIADO.getStatusId());
				inBoxList.forEach(outbox -> {
				outBoxRepository.updateStatus(outbox.getMessage().getId(), StatusEnum.SPAN.getStatusId());
			});
			
			}
		
		
		}catch(Exception e) {
			logger.error("Error update to spam");
		}
		
		return inBoxList;
	}	
	
	
	/**
	 * Save outbox and message asociated
	 * @param email
	 * @return
	 * @throws NoAddressDomainException
	 */
	private Email saveOutBoxEmail(OutBox email) throws NoAddressDomainException{
		OutBox savedEmail = null;
		Address address = getValidatedAddress(email.getMessage().getEmailFrom());
			if(address != null) {
			savedEmail = outBoxRepository.save(email);
			Message savedMessage= mesageClient.saveMessage(email.getMessage()).getBody();
			savedEmail.setMessage(savedMessage);
		}
		return savedEmail;
	}
	
	
	/**
	 * Retrieves {@link Address} a string address if not exsit retrieves null
	 * @param stringAddress
	 * @return
	 * @throws MailServiceException
	 */
	private Address getAddress(String stringAddress) {
		return  addressClient.getAddressByEmailAddress(stringAddress).getBody();
	}
	
	/**
	 * Retrieves {@link Address} by a string address if not exsits trhows {@link NoAddressDomainException} 
	 * @param stringAddress
	 * @return
	 * @throws MailServiceException
	 */
	private Address getValidatedAddress(String stringAddress) throws NoAddressDomainException{
		Address address = null;
		address = getAddress(stringAddress);
		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}
		return address;
	}	
	

}
