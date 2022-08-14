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

import com.example.emailbox.dto.EmailAddressKeyDTO;
import com.example.emailbox.dto.MailDTO;
import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.EmailAddressKey;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.mappers.EMailMapper;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.service.InBoxService;
import com.example.emailbox.service.OutBoxService;

@RestController
@RequestMapping(value = "/api/inbox")
public class InBoxController {

	Logger logger = LoggerFactory.getLogger(InBoxController.class);



	@Autowired
	private InBoxService inBoxService;

	@Autowired
	private OutBoxService outBoxService;
	

	@GetMapping
	public ResponseEntity<MailDTO> getInBoxById(@RequestBody EmailAddressKeyDTO emailAddressKeyDTO) throws MailServiceException {

		Email mail = inBoxService.getInBoxMailById(new EmailAddressKey(emailAddressKeyDTO.getMessageId(), emailAddressKeyDTO.getAddressId()));
		if (mail != null) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mail));
		}
		return ResponseEntity.noContent().build();
	}

	
	@GetMapping("/{messageId}")
	public ResponseEntity<Set<MailDTO>> getInBoxById(@PathVariable("messageId") Long messageId) throws MailServiceException {

		Set<Email> mailSet = inBoxService.emailListByMessageId(messageId);
		if (!mailSet.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mailSet));
		}
		return ResponseEntity.noContent().build();
	}

	
	@PutMapping(value = "updateOutBoxStatusMail/{mailId}/{statusId}")
	public ResponseEntity<MailDTO> updateOutBoxStatusMail(@PathVariable("mailId") Long mailId,@PathVariable("statusId") int statusId)
			throws MailServiceException {
		Email emailUpdated = null;

		try {

			emailUpdated = outBoxService.updateMailStatus(mailId,  StatusEnum.of(statusId));
			//call message client
			if (emailUpdated == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			throw new MailServiceException(e, new Object[] { mailId });
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(emailUpdated));
	}

	
	
	@PostMapping("/deleteInBox/{mailId}")
	public ResponseEntity<MailDTO> deleteInBox(@PathVariable("mailId") Long mailId,@PathVariable("addressId") Long addressId) {
		Email deletedMail = null;

		try {

			deletedMail = inBoxService.deleteInBox(mailId,addressId);

			if (deletedMail == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(deletedMail));
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
	
	/**
	 * Send a mail by Id
	 * @param mailId
	 * @return
	 * @throws MailServiceException
	 * @throws EmailStatusException
	 */
	@PostMapping(value = "/sendMailById/{mailId}")
	public ResponseEntity<MailDTO> sendMailById(@PathVariable("mailId") Long mailId)
			throws MailServiceException, EmailStatusException {
		Email mailSended = null;

		mailSended = inBoxService.sendMail(mailId);
		if (mailSended == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(mailSended));
	}
	
	/**
	 * Update sent OutBox and InBox messages as SPAM by string email address
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
