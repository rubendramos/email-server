package com.example.emailbox.controller;

import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "/api/inbox")
public class InBoxController {

	Logger logger = LoggerFactory.getLogger(InBoxController.class);



	@Autowired
	private InBoxService inBoxService;

	@Autowired
	private OutBoxService outBoxService;
	

	@GetMapping("/inbox")
	public ResponseEntity<Set<MailDTO>> listIntBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException {

		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Email> mails = inBoxService.listEmailsFromAddresAndStatus(addressParam, statusEnum);
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}




	@PutMapping(value = "updateMailMessage/{mailId}")
	public ResponseEntity<MailDTO> updateMailMessage(@PathVariable("mailId") Long mailId, @RequestBody MailDTO mailDTO)
			throws MailServiceException {
		Message messaUpdated = null;
		Email emailUpdated = null;

		try {

			mailDTO.setMailId(mailId);
			emailUpdated = EMailMapper.convertToEntity(mailDTO);
			//messaUpdated = messageService.updateMail(EMailMapper.convertToEntity(mailDTO).getMessage());
			//call message client
			if (messaUpdated == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			emailUpdated.setMessage(messaUpdated);
			
			//TODO call meesage cliente
			//emailUpdated.setEmailStatusValue(messaUpdated.getOutBox().getEmailStatusValue());

		} catch (Exception e) {
			throw new MailServiceException(e, new Object[] { mailId });
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(emailUpdated));
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

	@PostMapping(value = "/setAsSpam/{emailAddress}")
	public ResponseEntity<Set<MailDTO>> setAsSpam(@PathVariable("emailAddress") String emailAddress)
			throws MailServiceException, EmailStatusException {
		Set<Email> updatedMails = null;

		updatedMails = inBoxService.setInBoxMailsAsSpam(emailAddress);
		if (updatedMails == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(updatedMails));
	}
}
