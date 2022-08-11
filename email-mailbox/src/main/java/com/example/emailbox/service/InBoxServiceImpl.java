package com.example.emailbox.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.emailbox.client.AddressClient;
import com.example.emailbox.client.MessageClient;
import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.EmailAddressKey;
import com.example.emailbox.entity.InBox;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.exceptions.NoAddressDomainException;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.AddressTypeEnum;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.repository.InBoxRepository;
import com.example.emailbox.repository.OutBoxRepository;
import com.example.emailbox.utils.EmailServerUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InBoxServiceImpl implements InBoxService {

	Logger logger = LoggerFactory.getLogger(InBoxServiceImpl.class);

	@Autowired
	AddressClient addressClient;
	
	@Autowired
	MessageClient mesageClient;
	
	private final InBoxRepository inBoxRepository;
	private final OutBoxRepository outBoxRepository;
	


	@Override	
	public Set<Email> listEmailsFromAddresAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {
		
		Set<Email> inBoxList = null;
		Address address = getValidatedAddress(stringAddress);
		if(address != null) {
			inBoxList  = inBoxRepository.findByAddressAndEmailStatusValue(address, status.getStatusId());
		}
		return inBoxList;
	}

	@Override
	public Set<Email> listEmailsFromAddres(String stringAddress) throws MailServiceException {
		Set<Email> inBoxList = null;
		Address address = getValidatedAddress(stringAddress);
		if(address != null) {
			 inBoxList = inBoxRepository.findByAddress(address);
		}
		return inBoxList;

	}

	@Override
	public Email sendMail(Long mailId) throws MailServiceException, EmailStatusException {
		OutBox outBoxMail = null;
		Message message = null;
		List<InBox> recipients = new ArrayList<>();

		try {

			
			outBoxMail = outBoxRepository.findById(mailId).get();
			message = mesageClient.getMessageById(outBoxMail.getMessage().getId()).getBody();
			outBoxMail.setMessage(message);
			
			Set<String> addressStringToSet = EmailServerUtils.getAddressSet(message.getEmailTo());
			Set<String> addressStringCcSet = EmailServerUtils.getAddressSet(message.getEmailCc());

			Set<Address> validAddressTo = validateAddressSet(addressStringToSet);
			Set<Address> validAddressCc = validateAddressSet(addressStringCcSet);

			Set<InBox> inBoxTo = generateInboxFromAddress(outBoxMail.getMessage(), validAddressTo, AddressTypeEnum.TO,
					StatusEnum.ENVIADO);
			Set<InBox> inBoxCC = generateInboxFromAddress(outBoxMail.getMessage(), validAddressCc, AddressTypeEnum.CC,
					StatusEnum.ENVIADO);

			recipients.addAll(inBoxTo);
			recipients.addAll(inBoxCC);

			if (outBoxMail.getEmailStatus() == StatusEnum.BORRADOR) {
				outBoxMail.setEmailStatusValue(StatusEnum.ENVIADO.getStatusId());
				
				inBoxRepository.saveAll(recipients);
				outBoxRepository.save(outBoxMail);

			} else {
				throw new EmailStatusException(
						"Mail Status : " + outBoxMail.getEmailStatus() + ". Mail will not be sended",
						new Object[] { outBoxMail });
			}

		} catch (Exception e) {
			logger.error("Error saving mail with id:" + mailId);
			throw new MailServiceException(e, new Object[] { "Error sending mail with id: " + mailId, outBoxMail });
		}

		return outBoxMail;

	}


	@Override
	public InBox deleteInBox(Long id, Long address) {
		InBox ematilToDeleted = null;
		EmailAddressKey eak = new EmailAddressKey();
		eak.setMessageId(id);
		eak.setAddressId(address);
		ematilToDeleted = inBoxRepository.findById(eak);
		if (null != ematilToDeleted && ematilToDeleted.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {

			ematilToDeleted.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			inBoxRepository.save(ematilToDeleted);
		} else {
			ematilToDeleted = null;
		}

		return ematilToDeleted;
	}

	@Override
	public Set<Email> deleteInBox(Set<Long> mailsIds, Long address) {
		Set<Email> deletedMails = new HashSet<>();

		mailsIds.forEach(mailId -> {
			Email deleteMail = deleteInBox(mailId, address);
			if (null != deleteMail) {
				deletedMails.add(deleteMail);
			}
		});

		return deletedMails;
	}
	
	
	/**
	 * Marca como spam todo los mails enviados desde una dirección 
	 * @param mailsIds
	 * @return
	 * @throws MailServiceException
	 */
	@Transactional
	public Set<Email> setInBoxMailsAsSpam(String stringAddress) throws MailServiceException{
		Set<Email> inBoxList = null; 
		
		try {
		
			Address address = getValidatedAddress(stringAddress);
			if(address != null) {
				inBoxList = inBoxRepository.findByAddressAndEmailStatusValue(address,StatusEnum.ENVIADO.getStatusId());
				inBoxList.forEach(outbox -> {
				inBoxRepository.updateStatus(outbox.getMessage().getId(), StatusEnum.SPAN.getStatusId());
			});
			
			}
		
		
		}catch(Exception e) {
			logger.error("Error update to spam");
		}
		
		return inBoxList;
	}	
	
	/**
	 * Update status from inBox email
	 * @param mailId
	 * @param addressId
	 * @param status
	 * @return
	 * @throws MailServiceException
	 */
	public Email updateMailStatus(Long mailId, Long addressId , StatusEnum status) throws MailServiceException{
		InBox ematilToUpdate = null;
		EmailAddressKey eak = new EmailAddressKey();
		eak.setMessageId(mailId);
		eak.setAddressId(addressId);
		ematilToUpdate = inBoxRepository.findById(eak);
		
		if (null != ematilToUpdate && ematilToUpdate.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {

			ematilToUpdate.setEmailStatusValue(status.getStatusId());
			inBoxRepository.save(ematilToUpdate);
		} else {
			ematilToUpdate = null;
		}
		
		return ematilToUpdate;
	}

	
	private Set<Address> validateAddressSet(Set<String> stringAddressSet) {
		Set<Address> validAddress = new HashSet<>();
		stringAddressSet.forEach(stringAddress -> {
			Address address = getAddress(stringAddress);
			if (null != address) {
				validAddress.add(address);
			}
		});

		return validAddress;
	}

	
	/**
	 * Generates a Inbox email from Address
	 * @param mail
	 * @param addressSet
	 * @param addressTypeEnum
	 * @param status
	 * @return
	 */
	private Set<InBox> generateInboxFromAddress(Message mail, Set<Address> addressSet, AddressTypeEnum addressTypeEnum,
			StatusEnum status) {
		Set<InBox> recipients = new HashSet<>();
		if (addressSet != null && !addressSet.isEmpty()) {
			addressSet.forEach(address -> {
				recipients.add(new InBox(mail, address, addressTypeEnum, status));
			});
		}
		return recipients;
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
	private Address getValidatedAddress(String stringAddress) throws MailServiceException{
		Address address = null;
		address = getAddress(stringAddress);
		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}
		return address;
	}	
}
