package com.example.emailbox.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

/**
 * @author rdr
 * @Since 13 ago 2022
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class InBoxServiceImpl implements InBoxService {

	Logger logger = LoggerFactory.getLogger(InBoxServiceImpl.class);
	private static final String ERROR_DELETE_INTBOX_MESSAGE = "Error deleting inbox message with ID = %s.";
	private static final String ERROR_GETTING_ADDRESS_AND_MESSAGE = "Error getting  address and messag from message with ID = %s.";

	@Autowired
	AddressClient addressClient;

	@Autowired
	MessageClient messageClient;

	private final InBoxRepository inBoxRepository;
	private final OutBoxRepository outBoxRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.InBoxService#listEmailsFromAddresAndStatus(java.
	 * lang.String, com.example.emailbox.modelo.enums.StatusEnum)
	 */
	@Override
	public Set<Email> listEmailsFromAddressAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {

		Set<Email> inBoxList = null;
		Address address = getValidatedAddress(stringAddress);
		if (address != null) {
			inBoxList = inBoxRepository.findByIdAddressIdAndEmailStatusValue(address.getId(), status.getStatusId());
			addMessageAndAddresToEmailList(inBoxList, address);
		}
		return inBoxList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.InBoxService#listEmailsFromAddres(java.lang.
	 * String)
	 */
	@Override
	public Set<Email> listEmailsFromAddress(String stringAddress) throws MailServiceException {
		Set<Email> inBoxList = null;
		Address address = getValidatedAddress(stringAddress);
		if (address != null) {
			inBoxList = inBoxRepository.findByIdAddressId(address.getId());
			addMessageAndAddresToEmailList(inBoxList, address);
		}
		return inBoxList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.InBoxService#listEmailsFromAddres(java.lang.
	 * String)
	 */
	@Override
	public Set<Email> emailListByMessageId(Long messageId) throws MailServiceException {
		Set<Email> inBoxList = null;
		inBoxList = inBoxRepository.findByIdMessageId(messageId);
		inBoxList.forEach(inBox -> {
			try {
				addMessageAndAddres(inBox);
			} catch (MailServiceException e) {
				logger.error(String.format(ERROR_GETTING_ADDRESS_AND_MESSAGE, messageId));
			}
		});

		return inBoxList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.emailbox.service.InBoxService#sendMail(java.lang.Long)
	 */
	@Override
	public Email sendMail(Long mailId) throws MailServiceException, EmailStatusException {
		OutBox outBoxMail = null;
		List<InBox> recipients = new ArrayList<>();

		try {
			
			outBoxMail = getOutBoxById(mailId);

			if (outBoxMail.getEmailStatus() == StatusEnum.BORRADOR) {

				recipients = generateRecipientsFromOutBox(outBoxMail);
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
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.emailbox.service.InBoxService#deleteInBox(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public Email deleteInBox(Long id, Long address) throws MailServiceException {
		InBox ematilToBeDeleted = null;
		Email emailDeleted = null;
		EmailAddressKey eak = new EmailAddressKey();
		eak.setMessageId(id);
		eak.setAddressId(address);
		ematilToBeDeleted = inBoxRepository.findById(eak);

		if (null != ematilToBeDeleted
				&& ematilToBeDeleted.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {
			ematilToBeDeleted.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			emailDeleted = inBoxRepository.save(ematilToBeDeleted);
		}

		return addMessageAndAddres(emailDeleted);
	}

	@Override
	public Set<Email> deleteInBox(Set<Long> mailsIds, Long address) {
		Set<Email> deletedMails = new HashSet<>();

		mailsIds.forEach(mailId -> {
			try {
				Email deleteMail = deleteInBox(mailId, address);
				if (null != deleteMail) {
					deletedMails.add(deleteMail);
				}
			} catch (MailServiceException e) {
				logger.error(String.format(ERROR_DELETE_INTBOX_MESSAGE, mailId));
			}
		});

		return deletedMails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.InBoxService#updateMailStatus(java.lang.Long,
	 * java.lang.Long, com.example.emailbox.modelo.enums.StatusEnum)
	 */
	@Override
	public Email updateMailStatus(Long mailId, Long addressId, StatusEnum status) throws MailServiceException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.InBoxService#getInBoxMailById(java.lang.Long)
	 */
	public Email getInBoxMailById(EmailAddressKey emailAddresKey) throws MailServiceException {
		return addMessageAndAddres(inBoxRepository.findById(emailAddresKey));
	}
	
	
	/**
	 * Load a full OuBox Eamil with message and addess
	 * @param messageId
	 * @return
	 */
	private OutBox getOutBoxById(Long messageId) {
		OutBox outBoxMail = outBoxRepository.findById(messageId).get();
		outBoxMail.setMessage(messageClient.getMessageById(outBoxMail.getId()).getBody());
		outBoxMail.setAddress(addressClient.getAddressById(outBoxMail.getAddressId()).getBody());
		return outBoxMail;
	}
	

	/**
	 * Add Message and Address to Inbox {@link Email} {@link Set}
	 * 
	 * @param inBoxList
	 * @param address
	 */
	private void addMessageAndAddresToEmailList(Set<Email> inBoxList, Address address) {

		try {

			inBoxList.forEach(email -> {
				Message message = messageClient.getMessageById(((InBox) email).getId().getMessageId()).getBody();
				email.setMessage(message);
				email.setAddress(address);
			});

		} catch (Exception e) {
			logger.error("Error setting message to email");
		}
	}

	/**
	 * Add {@link Message} and {@link Address} to a {@link OutBox} email List
	 * 
	 * @param outBoxList
	 * @param address
	 */
	private Email addMessageAndAddres(Email email) throws MailServiceException {

		try {
			Message message = messageClient.getMessageById(((InBox) email).getId().getMessageId()).getBody();
			Address address = addressClient.getAddressById(((InBox) email).getId().getAddressId()).getBody();
			email.setMessage(message);
			email.setAddress(address);

		} catch (Exception e) {
			logger.error("Error setting message to email");
			throw new MailServiceException(e, new Object[] { email });
		}

		return email;
	}

	
	
	/**
	 * Generate recipients {@link InBox} from To and CC list of addresses
	 * @param outBoxMail
	 * @return
	 */
	private List<InBox> generateRecipientsFromOutBox(OutBox outBoxMail) {
		List<InBox> recipients = new ArrayList<>();
		Message message = outBoxMail.getMessage();

		Set<InBox> inBoxTo = generateInboxByAddressType(message, AddressTypeEnum.TO);
		Set<InBox> inBoxCC = generateInboxByAddressType(message, AddressTypeEnum.CC);

		recipients.addAll(inBoxTo);
		recipients.addAll(inBoxCC);
		
		return recipients;
	}
	
	/**
	 * Generate Inbox by AddressTypeEnum
	 * @param message
	 * @param addressTypeEnum
	 * @return
	 */
	private Set<InBox> generateInboxByAddressType(Message message, AddressTypeEnum addressTypeEnum){
		Set<String> addressStringToSet = EmailServerUtils.getAddressSet(message.getEmailTo());
		Set<Address> validAddress = validatedAddressSet(addressStringToSet);
		return generateInboxFromAddress(message, validAddress, addressTypeEnum, StatusEnum.ENVIADO);
	}
	
	
	/**
	 * Add to a set all valid Addess
	 * 
	 * @param stringAddressSet
	 * @return
	 */
	private Set<Address> validatedAddressSet(Set<String> stringAddressSet) {
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
	 * Generates a {@link InBox} {@link Email} from {@link Address}, {@link Message}
	 * and {@link AddressTypeEnum}
	 * 
	 * @param mail
	 * @param addressSet
	 * @param addressTypeEnum
	 * @param status
	 * @return
	 */
	private Set<InBox> generateInboxFromAddress(Message message, Set<Address> addressSet, AddressTypeEnum addressTypeEnum,
			StatusEnum status) {
		Set<InBox> recipients = new HashSet<>();
		if (addressSet != null && !addressSet.isEmpty()) {
			addressSet.forEach(address -> {
				recipients.add(new InBox(message, address, addressTypeEnum, status));
			});
		}
		return recipients;
	}

	/**
	 * Retrieves {@link Address} by a string email address. If not exits return
	 * null.
	 * 
	 * @param stringAddress
	 * @return
	 * @throws MailServiceException
	 */
	private Address getAddress(String stringAddress) {
		return addressClient.getAddressByEmailAddress(stringAddress).getBody();
	}

	/**
	 * Retrieves {@link Address} by a string address if not exsits trhows
	 * {@link NoAddressDomainException}
	 * 
	 * @param stringAddress
	 * @return
	 * @throws MailServiceException
	 */
	private Address getValidatedAddress(String stringAddress) throws MailServiceException {
		Address address = null;
		address = getAddress(stringAddress);
		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}
		return address;
	}
}
