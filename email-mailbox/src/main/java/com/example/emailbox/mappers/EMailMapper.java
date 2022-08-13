package com.example.emailbox.mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.emailbox.dto.MailDTO;
import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.InBox;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.AddressTypeEnum;
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

//		message.setOutBox(outBox);
		
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
	
	/**
	 * Add an address to TO,CC or BCC List depends on AddressTypeEnum
	 * 
	 * @param address
	 */
	private static void addEmailAddressFromRecipients(MailDTO mailDTO, Set<InBox> emailAddressSet) {

		emailAddressSet.forEach(emailAddress -> {
			switch (emailAddress.getEmailAddressType()) {
			case TO:
				mailDTO.getEmailTo().add(emailAddress.getAddress().getAddress());
				break;
			case CC:
				mailDTO.getEmailCc().add(emailAddress.getAddress().getAddress());
				break;
			case BCC:
				break;
			}
		});
	}
	
	
	private static OutBox addSenderFromMailDTO(Message mail, MailDTO mailDTO){
		Address senderAddress = Address.builder().address(mailDTO.getEmailFrom()).build();
		return new OutBox(mail, senderAddress, mailDTO.getEmailStatus());
	}
	
	private static Set<InBox> addRecipientsFromMailDTO(Message mail, MailDTO mailDTO){
		Set<InBox> recipients = new HashSet<>();
//		recipients.addAll(getEmailAddress(mail,mailDTO.getEmailTo(), AddressTypeEnum.TO, mail.getOutBox().getEmailStatus()));
//		recipients.addAll(getEmailAddress(mail,mailDTO.getEmailCc(), AddressTypeEnum.BCC, mail.getOutBox().getEmailStatus()));
		return recipients;
	}
	
	private static Set<InBox> getEmailAddress(Message mail, Set<String> addressList, AddressTypeEnum addressTypeEnum, StatusEnum status ){
		Set<InBox> recipients = new HashSet<>();
			if(addressList != null && !addressList.isEmpty()) {
			addressList.forEach(addressString-> {
				Address address = Address.builder().address(addressString).build();
				recipients.add(new InBox(mail, address, addressTypeEnum, status));
			});
		}
		return recipients;	
	}
	
}
