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
	
	public static MailDTO convertToDTO(Email email) {
		return parseMailToMailDTO(email);
	}
	
	
	public static Set<MailDTO> convertToDTO(Set<Email> mailList) {
		return parseMailToMailDTOList(mailList);
	}
	
	public static Email convertToEntity(MailDTO mailDTO) {
		
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
	
	private static Set<MailDTO> parseMailToMailDTOList(Set<Email> emailList) {
		Set<MailDTO> mailDTOList = new HashSet<>();
		emailList.forEach(email -> mailDTOList.add(parseMailToMailDTO(email)));
		return mailDTOList;
	}
	
}
