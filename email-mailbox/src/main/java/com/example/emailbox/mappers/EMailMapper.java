package com.example.emailbox.mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.emailbox.dto.MailDTO;
import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;

public class EMailMapper {

	private static final String MAIL_ADDRESS_SEPARATOR=","; 
	
	/**
	 * Convert Email Entity in a mailDTO 
	 * @param email
	 * @return
	 */
	public static MailDTO convertToDTO(Email email) {
		return parseMailToMailDTO(email);
	}
	
	/**
	 * Convert Email Entity Set in a mailDTO Set 
	 * @param mailList
	 * @return
	 */
	public static Set<MailDTO> convertToDTO(Set<Email> mailList) {
		return parseMailToMailDTOList(mailList);
	}
	
	/**
	 * Convert a mailDTO in a Email Entity
	 * @param mailDTO
	 * @return
	 */
	public static Email convertToEntity(MailDTO mailDTO) {
		return parseMailDTOToMail(mailDTO);
	}
	
	
	/**
	 * Convert a mailDTO Set in a Email Entity Set
	 * @param mailDTOSet
	 * @return
	 */
	public static Set<Email> convertToEntity(Set<MailDTO> mailDTOSet) {
		return parseMailDTOToEmailList(mailDTOSet);
	}
	
	
	
	/**
	 * Convert a mailDTO in a Email Entity
	 * @param mailDTO
	 * @return
	 */
	private static Email parseMailDTOToMail(MailDTO mailDTO) {
		Message message = Message.builder().createAt(mailDTO.getCreateAt())				
				.emailBody(mailDTO.getEmailBody())
				.emailFrom(mailDTO.getEmailFrom())
				.emailTo(String.join(MAIL_ADDRESS_SEPARATOR,mailDTO.getEmailTo()))
				.emailCc(String.join(MAIL_ADDRESS_SEPARATOR,mailDTO.getEmailCc()))
				.updateAt(mailDTO.getUpdateAt())
				.id(mailDTO.getMailId())
				.build();
				
		Address senderAddress = Address.builder().address(mailDTO.getEmailFrom()).build();
		OutBox outBox = new OutBox(message,senderAddress,mailDTO.getEmailStatus());
		return outBox;
	}

	/**
	 * Convert a mailDTO Set in a Email Entity Set
	 * @param mailDTOList
	 * @return
	 */
	private static Set<Email> parseMailDTOToEmailList(Set<MailDTO> mailDTOList) {
		Set<Email> eMailList = new HashSet<>();
		mailDTOList.forEach(email -> eMailList.add(parseMailDTOToMail(email)));
		return eMailList;
	}


	/**
	 *  Convert Email Entity in a mailDTO 
	 * @param email
	 * @return
	 */
	private static MailDTO parseMailToMailDTO(Email email) {
		Message message = email.getMessage();
		MailDTO mailDTO = MailDTO.builder().mailId(message.getId())
				.emailBody(message.getEmailBody())				
				.createAt(message.getCreateAt())
				.updateAt(message.getUpdateAt())
				.emailFrom(email.getAddress().getAddress())
				.emailTo(new HashSet<String>(Arrays.asList(message.getEmailTo().split(MAIL_ADDRESS_SEPARATOR))))
				.emailCc(new HashSet<String>(Arrays.asList(message.getEmailCc().split(MAIL_ADDRESS_SEPARATOR))))
				.emailStatus(StatusEnum.of(email.getEmailStatusValue())).build();
		return mailDTO;
	}

	/**
	 *  Convert Email Entity Set in a mailDTO Set 
	 * @param emailList
	 * @return
	 */
	private static Set<MailDTO> parseMailToMailDTOList(Set<Email> emailList) {
		Set<MailDTO> mailDTOList = new HashSet<>();
		emailList.forEach(email -> mailDTOList.add(parseMailToMailDTO(email)));
		return mailDTOList;
	}
	
}
