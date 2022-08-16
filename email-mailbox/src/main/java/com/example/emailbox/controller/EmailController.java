package com.example.emailbox.controller;

import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailbox.dto.MailBoxDTO;
import com.example.emailbox.dto.MailDTO;
import com.example.emailbox.dto.MultipleDeleteDTO;
import com.example.emailbox.entity.Email;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.mappers.EMailMapper;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.service.InBoxService;
import com.example.emailbox.service.OutBoxService;

@RestController
@RequestMapping(value = "/api/mailbox")
public class EmailController {

	Logger logger = LoggerFactory.getLogger(EmailController.class);



	@Autowired
	private InBoxService inBoxService;

	@Autowired
	private OutBoxService outBoxService;


	@GetMapping
	public ResponseEntity<Set<MailDTO>> searchMailBox(@RequestBody @Valid MailBoxDTO mailBoxDTO) throws MailServiceException {
		Set<Email> mails = null;
		StatusEnum statusEnum = mailBoxDTO.getEmailStatus();
		String emailAdress = mailBoxDTO.getEmailAddress();

		switch (mailBoxDTO.getMailBoxType()) {
		case INBOX:
			mails = inBoxService.listEmailsFromAddressAndStatus(emailAdress, statusEnum);
			break;
		case OUTBOX:
			mails = outBoxService.listEmailsFromAddressAndStatus(emailAdress, statusEnum);
		}

		if (!mails.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}
	
	
	/**
	 * Create a mail from a {@link MailDTO}
	 * 
	 * @param mailDTO
	 * @return
	 */
	@PostMapping
	public ResponseEntity<MailDTO> createAndSendMail(@RequestBody @Valid MailDTO mailDTO) throws MailServiceException{
		Email savedMail = null;
		

		if (mailDTO != null) {
			Email mail = EMailMapper.convertToEntity(mailDTO);
			savedMail = outBoxService.createOutBox(mail);
			savedMail = inBoxService.sendMail(savedMail.getMessage().getId());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMail));
	}


	@PutMapping(value = "/updateMailMessage/{mailId}")
	public ResponseEntity<MailDTO> updateMailMessage(@PathVariable("mailId") Long mailId, @RequestBody MailDTO mailDTO)
			throws MailServiceException {

		Email emailToBeUpdated = null;
		Email emailUpdated = null;

		try {

			mailDTO.setMailId(mailId);
			emailToBeUpdated = EMailMapper.convertToEntity(mailDTO);

			emailUpdated = outBoxService.updateOutBoxMessage(emailToBeUpdated);

		} catch (Exception e) {
			throw new MailServiceException(e, new Object[] { mailId });
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(emailUpdated));
	}
	
	@PostMapping("/deleteMultiple")
	public ResponseEntity<Set<MailDTO>> deleteMultipleMail(@RequestBody MultipleDeleteDTO emailsIds) {
		Set<Email> deletedMails = null;

		try {

			switch (emailsIds.getMailBoxType()) {
			case INBOX:
				deletedMails = inBoxService.deleteInBox(emailsIds.getEmailIDs(), emailsIds.getAddressId());
				break;
			case OUTBOX:
				deletedMails = outBoxService.deleteOutBox(emailsIds.getEmailIDs());
			}

			if (deletedMails == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(deletedMails));
	}

	/**
	 * Set as spam All mails sent from an @param emailAddress
	 * 
	 * @param emailAddress
	 * @return
	 * @throws MailServiceException
	 * @throws EmailStatusException
	 */
	@PostMapping(value = "/setAsSpam/{emailAddress}")
	public ResponseEntity<Set<MailDTO>> setAsSpam(@PathVariable("emailAddress") String emailAddress)
			throws MailServiceException, EmailStatusException {
		Set<Email> updatedMails = null;

		updatedMails = outBoxService.updateMailsAsSpam(emailAddress);
		if (updatedMails == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(updatedMails));
	}
	
}
