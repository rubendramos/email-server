package com.example.emailbox.controller;

import java.util.HashSet;
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
import com.example.emailbox.dto.MultipleEmailDTO;
import com.example.emailbox.entity.Email;
import com.example.emailbox.exceptions.EmailStatusException;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.mappers.EMailMapper;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.service.OutBoxService;

@RestController
@RequestMapping(value = "/emailBox")
public class OutBoxController {

	Logger logger = LoggerFactory.getLogger(OutBoxController.class);

	@Autowired
	private OutBoxService outBoxService;


	/**
	 * Get a set of outBox mails form addres and status
	 * @param addressParam
	 * @param statusParam
	 * @return
	 * @throws MailServiceException
	 */
	@GetMapping("/outbox")
	public ResponseEntity<Set<MailDTO>> listOutBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException {
		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Email> mails = outBoxService.listEmailsFromAddresAndStatus(addressParam, statusEnum);
		if (!mails.isEmpty()) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mails));
		}
		return ResponseEntity.noContent().build();
	}

	
	/**
	 * Create a mail from a {@link MailDTO}
	 * @param mailDTO
	 * @return
	 */
	@PostMapping
	public ResponseEntity<MailDTO> createMail(@RequestBody MailDTO mailDTO) {

		Email savedMail = null;

		try {

			if (mailDTO != null) {
				Email mail = EMailMapper.convertToEntity(mailDTO);
				savedMail = outBoxService.createOutBox(mail);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMail));
	}


	/**
	 * Create multiple mails
	 * @param multipleMailDTO
	 * @return
	 */
	@PostMapping("createMultipleEmails")
	public ResponseEntity<Set<MailDTO>> createMail(@RequestBody MultipleEmailDTO multipleMailDTO) {

		Set<Email> savedMailSet = null;

		try {

			if (multipleMailDTO != null && null != multipleMailDTO.getEmailsDTOs()
					&& !multipleMailDTO.getEmailsDTOs().isEmpty()) {
				savedMailSet = saveMailSet(multipleMailDTO.getEmailsDTOs());
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMailSet));
	}



	/**
	 * Update the mail status of a outBox mail 
	 * @param mailId
	 * @param statusId
	 * @return
	 * @throws MailServiceException
	 */
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

	
	/**
	 * Delete OutBox Email
	 * @param mailId
	 * @return
	 */
	@PostMapping("/deleteOutBox/{mailId}")
	public ResponseEntity<MailDTO> deleteOutBox(@PathVariable("mailId") Long mailId) {
		Email deletedMail = null;

		try {

			deletedMail = outBoxService.deleteOutBox(mailId);

			if (deletedMail == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(deletedMail));
	}

	/**
	 * Set as spall All mails sent from a emailAddress
	 * @param emailAddress
	 * @return
	 * @throws MailServiceException
	 * @throws EmailStatusException
	 */
	@PostMapping(value = "/setAsSpam/{emailAddress}")
	public ResponseEntity<Set<MailDTO>> setAsSpam(@PathVariable("emailAddress") String emailAddress)
			throws MailServiceException, EmailStatusException {
		Set<Email> updatedMails = null;

		updatedMails = outBoxService.setOutBoxMailsAsSpam(emailAddress);
		if (updatedMails == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(EMailMapper.convertToDTO(updatedMails));
	}
	

	/**
	 * Save a set of Mails(Outbox)
	 * @param mailDTOSet
	 * @return
	 */
	private Set<Email> saveMailSet(Set<MailDTO> mailDTOSet) {
		Set<Email> savedMailSet = new HashSet<>();
		mailDTOSet.forEach(mailDTO -> {
			try {
				Email savedMail = null;
				Email mail = EMailMapper.convertToEntity(mailDTO);
				savedMail = outBoxService.createOutBox(mail);
				if (null != savedMail) {
					savedMailSet.add(savedMail);
				}
			} catch (Exception e) {
				logger.warn("No se ha podido guardar el mail con sender: " + mailDTO.toString());
			}
		});

		return savedMailSet;
	}

}
