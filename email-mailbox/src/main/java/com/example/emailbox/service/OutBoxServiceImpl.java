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
import com.example.emailbox.dto.MailDTO;
import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.exceptions.NoAddressDomainException;
import com.example.emailbox.mappers.EMailMapper;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.repository.InBoxRepository;
import com.example.emailbox.repository.OutBoxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutBoxServiceImpl implements OutBoxService {

	private static final String INVALID_UPDATE_STATUS_MESSAGE = "Incorrect Email status to be updated. Email status = %s.";
	private static final String ERROR_DELETE_OUTBOX_MESSAGE = "Error deleting outbox message with ID = %s.";
	private static final String ERROR_UPDATE_TO_SPAM = "Error updating to spam messages from address = %s.";
	private static final String ERROR_UPDATE_MESSAGE = "Error updating message with ID = %s.";
	private static final String ERROR_CREATE_OUTBOX = "Error creatting outbox.";

	
	Logger logger = LoggerFactory.getLogger(OutBoxServiceImpl.class);
	private final OutBoxRepository outBoxRepository;	
	private final InBoxRepository inBoxRepository;

	@Autowired
	AddressClient addressClient;

	@Autowired
	MessageClient messageClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.emailbox.service.OutBoxService#getOutBoxById(java.lang.Long)
	 */
	@Override
	public Email getOutBoxById(Long mailId) throws MailServiceException {
		return addMessageAndAddres(outBoxRepository.findById(mailId).get());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.OutBoxService#listEmailsFromAddresAndStatus(java
	 * .lang.String, com.example.emailbox.modelo.enums.StatusEnum)
	 */
	@Override
	public Set<Email> listEmailsFromAddressAndStatus(String stringAddress, StatusEnum status)
			throws MailServiceException {
		Set<Email> outBoxList = null;

		Address address = getAddress(stringAddress);
		if (address != null) {
			outBoxList = outBoxRepository.findByAddressIdAndEmailStatusValue(address.getId(), status.getStatusId());
			addMessageAndAddresToEmailList(outBoxList, address);
		}

		return outBoxList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.OutBoxService#listEmailsFromAddres(java.lang.
	 * String)
	 */
	@Override
	public Set<Email> listEmailsFromAddress(String stringAddress) throws MailServiceException {
		Set<Email> outBoxList = null;
		Address address = getValidatedAddress(stringAddress);
		if (address != null) {
			outBoxList = outBoxRepository.findByAddressId(address.getId());
			addMessageAndAddresToEmailList(outBoxList, address);
		}

		return outBoxList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.OutBoxService#createOutBox(com.example.emailbox.
	 * entity.Email)
	 */
	@Override
	public Email createOutBox(Email email) throws NoAddressDomainException {
		Email savedEmail = null;

		Address address = getValidatedAddress(email.getAddress().getAddress());
		if (address != null) {
			email.getMessage().setCreateAt(new Date());
			email.getMessage().setUpdateAt(null);
			email.setAddress(address);
			email.setEmailStatusValue(StatusEnum.BORRADOR.getStatusId());
			email.setEmailStatus(StatusEnum.BORRADOR);
			savedEmail = saveOutBoxEmail((OutBox) email);
		}
		return savedEmail;
	}

	
	
	/**
	 * Save a set of Mails(Outbox)
	 * 
	 * @param mailDTOSet
	 * @return
	 */
	public Set<Email> saveEmailBoxSet(Set<Email> emailSet) throws MailServiceException{
		Set<Email> savedMailSet = new HashSet<>();
		emailSet.forEach(mail -> {
			try {
				Email savedMail = null;
				savedMail = createOutBox(mail);
				if (null != savedMail) {
					savedMailSet.add(savedMail);
				}
			} catch (Exception e) {
				logger.warn("Mail will not be created: " + mail.toString());
				throw new RuntimeException(e);
			}
		});

		return savedMailSet;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.emailbox.service.OutBoxService#deleteOutBox(java.lang.Long)
	 */
	@Override
	public Email deleteOutBox(Long id) throws MailServiceException {
		Email ematilToDeleted = null;
		Email emailDeleted = null;
		ematilToDeleted = outBoxRepository.findById(id).get();
		
		if (null != ematilToDeleted && ematilToDeleted.getEmailStatusValue() != StatusEnum.ELIMINADO.getStatusId()) {
			ematilToDeleted.setEmailStatusValue(StatusEnum.ELIMINADO.getStatusId());
			emailDeleted = outBoxRepository.save((OutBox) ematilToDeleted);
		}

		return addMessageAndAddres(emailDeleted);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.emailbox.service.OutBoxService#deleteOutBox(java.util.Set)
	 */
	@Override
	public Set<Email> deleteOutBox(Set<Long> mailsIds) throws MailServiceException {
		Set<Email> deletedMails = new HashSet<>();

		mailsIds.forEach(mailId -> {
			try {
				Email deleteMail = deleteOutBox(mailId);
				if (null != deleteMail) {
					deletedMails.add(deleteMail);
				}
			} catch (MailServiceException e) {
				logger.error(String.format(ERROR_DELETE_OUTBOX_MESSAGE, mailId));
			}
		});

		return deletedMails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.OutBoxService#updateMailStatus(java.lang.Long,
	 * com.example.emailbox.modelo.enums.StatusEnum)
	 */
	public Email updateMailStatus(Long mailId, StatusEnum status) throws MailServiceException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.emailbox.service.OutBoxService#updateOutBoxMessage(com.example.
	 * emailbox.entity.Email)
	 */
	@Override
	public Email updateOutBoxMessage(Email email) throws MailServiceException {
		OutBox emaiToBeUpdated = null;
		Long messageId = email.getMessage().getId();
		emaiToBeUpdated = outBoxRepository.findById(messageId).get();

		if (emaiToBeUpdated.getEmailStatusValue() == StatusEnum.BORRADOR.getStatusId()) {
			email.getMessage().setUpdateAt(new Date());
			messageClient.updateMail(messageId, email.getMessage());
		} else {
			logger.info(String.format(INVALID_UPDATE_STATUS_MESSAGE, emaiToBeUpdated.getEmailStatus()));
			throw new EmailStatusException(
					String.format(INVALID_UPDATE_STATUS_MESSAGE, emaiToBeUpdated.getEmailStatus()),
					new Object[] { email });
		}

		return getOutBoxById(messageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.emailbox.service.InBoxService#setInBoxMailsAsSpam(java.lang.
	 * String)
	 */
	@Transactional
	@Override
	public Set<Email> updateMailsAsSpam(String stringAddress) throws MailServiceException {
		Set<Email> sendedMailsToBeUPdated = null;

		try {

			Address address = getValidatedAddress(stringAddress);
			if (address != null) {
				sendedMailsToBeUPdated = outBoxRepository.findByAddressIdAndEmailStatusValue(address.getId(),
						StatusEnum.ENVIADO.getStatusId());
				sendedMailsToBeUPdated.forEach(outbox -> {
					Long messageId = null;
					try {
						
						messageId = ((OutBox)outbox).getId();
						outBoxRepository.updateStatus(messageId, StatusEnum.SPAN.getStatusId());
						inBoxRepository.updateStatus(messageId, StatusEnum.SPAN.getStatusId());
						addMessageAndAddres(outbox);
						
					} catch (Exception e) {
						logger.error(String.format(ERROR_UPDATE_MESSAGE, messageId));
					}
				});
			}

		} catch (Exception e) {
			logger.error(String.format(ERROR_UPDATE_TO_SPAM, stringAddress));
			throw new MailServiceException(e, new Object[] { String.format(ERROR_UPDATE_TO_SPAM, stringAddress) });
		}

		return sendedMailsToBeUPdated;
	}


	/**
	 * Add Message and Addres to box email List
	 * 
	 * @param inBoxList
	 * @param address
	 */

	/**
	 * Add {@link Message} and {@link Address} to a {@link OutBox} email List
	 * 
	 * @param outBoxList
	 * @param address
	 */
	private void addMessageAndAddresToEmailList(Set<Email> outBoxList, Address address) {

		try {

			outBoxList.forEach(email -> {
				Message message = messageClient.getMessageById(((OutBox) email).getId()).getBody();
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
			Message message = messageClient.getMessageById(((OutBox) email).getId()).getBody();
			Address adress = addressClient.getAddressById(((OutBox) email).getAddressId()).getBody();
			email.setMessage(message);
			email.setAddress(adress);

		} catch (Exception e) {
			logger.error("Error setting message to email");
			throw new MailServiceException(e, new Object[] { email });
		}

		return email;
	}

	/**
	 * Save {@link OutBox}
	 * 
	 * @param email
	 * @return
	 * @throws NoAddressDomainException
	 */
	private Email saveOutBoxEmail(OutBox email) throws NoAddressDomainException {
		OutBox savedEmail = null;
		try {

			Address address = getValidatedAddress(email.getMessage().getEmailFrom());
			if (address != null) {
				Message savedMessage = messageClient.saveMessage(email.getMessage()).getBody();
				email.setMessage(savedMessage);
				email.setAddress(address);
				savedEmail = outBoxRepository.save(email);
				savedEmail.setAddress(address);
				savedEmail.setMessage(savedMessage);
			}

		} catch (Exception e) {
			logger.error("Error saving OutBox mail", e);
		}
		return savedEmail;
	}

	/**
	 * Retrieves {@link Address} by string emails address. If not exsit return null.
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
	private Address getValidatedAddress(String stringAddress) throws NoAddressDomainException {
		Address address = null;
		address = getAddress(stringAddress);
		if (address == null) {
			throw new NoAddressDomainException(new Object[] { stringAddress });
		}
		return address;
	}

}
