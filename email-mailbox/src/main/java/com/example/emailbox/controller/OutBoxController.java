package com.example.emailbox.controller;

import java.util.HashSet;
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
@RequestMapping(value = "/api/outbox")
public class OutBoxController {

	Logger logger = LoggerFactory.getLogger(OutBoxController.class);

	@Autowired
	private OutBoxService outBoxService;

	/**
	 * Get a set of outBox mails form addres and status
	 * 
	 * @param addressParam
	 * @param statusParam
	 * @return
	 * @throws MailServiceException
	 */
	@GetMapping("/{mailId}")

	public ResponseEntity<MailDTO> listOutBoxMails(@PathVariable("mailId") Long mailId)
			throws MailServiceException {
		Email mail = outBoxService.getOutBoxById(mailId);
		if (mail != null) {
			return ResponseEntity.ok(EMailMapper.convertToDTO(mail));
		}
		return ResponseEntity.noContent().build();
	}

	/**
	 * Get a set of outBox mails form addres and status
	 * 
	 * @param addressParam
	 * @param statusParam
	 * @return
	 * @throws MailServiceException
	 */
	@GetMapping("/outbox")
	public ResponseEntity<Set<MailDTO>> listOutBoxMails(@RequestParam String addressParam,
			@RequestParam int statusParam) throws MailServiceException {
		StatusEnum statusEnum = StatusEnum.of(statusParam);
		Set<Email> mails = outBoxService.listEmailsFromAddressAndStatus(addressParam, statusEnum);
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
	public ResponseEntity<MailDTO> createAnEMail(@RequestBody @Valid MailDTO mailDTO) throws MailServiceException {
		Email savedMail = null;

		if (mailDTO != null) {
			Email mail = EMailMapper.convertToEntity(mailDTO);
			savedMail = outBoxService.createOutBox(mail);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMail));
	}

	/**
	 * Create multiple mails
	 * 
	 * @param multipleMailDTO
	 * @return
	 */
	@PostMapping("/createMultipleEmails")
	public ResponseEntity<Set<MailDTO>> createMail(@RequestBody MultipleEmailDTO multipleMailDTO) {

		Set<Email> savedMailSet = null;

		try {

			if (multipleMailDTO != null && null != multipleMailDTO.getEmailsDTOs()
					&& !multipleMailDTO.getEmailsDTOs().isEmpty()) {
				
				Set<Email> mailSetToSave = EMailMapper.convertToEntity(multipleMailDTO.getEmailsDTOs());
				savedMailSet = outBoxService.saveEmailBoxSet(mailSetToSave);
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(EMailMapper.convertToDTO(savedMailSet));
	}

	/**
	 * Update the mail status of a outBox mail
	 * 
	 * @param mailId
	 * @param statusId
	 * @return
	 * @throws MailServiceException
	 */
	@PutMapping(value = "updateOutBoxStatusMail/{mailId}/{statusId}")
	public ResponseEntity<MailDTO> updateOutBoxStatusMail(@PathVariable("mailId") Long mailId,
			@PathVariable("statusId") int statusId) throws MailServiceException {
		Email emailUpdated = null;

		try {

			emailUpdated = outBoxService.updateMailStatus(mailId, StatusEnum.of(statusId));
			// call message client
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
	 * 
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
}
