package com.example.emailbox.mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.emailbox.dto.MailDTO;
import com.example.emailbox.entity.InBox;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;

public class MessageMapper {

	private static final String MAIL_ADDRESS_SEPARATOR=","; 
	
	public static MailDTO convertToDTO(Message mail) {
		return parseMailToMailDTO(mail);
	}
	
	
	public static Set<MailDTO> convertToDTO(Set<Message> mailList) {
		return parseMailToMailDTOList(mailList);
	}
	
	public static Message convertToEntity(MailDTO mailDTO) {
		
		Message message = Message.builder().createAt(mailDTO.getCreateAt())				
				.emailBody(mailDTO.getEmailBody())
				.emailFrom(mailDTO.getEmailFrom())
				.emailTo(String.join(MAIL_ADDRESS_SEPARATOR,mailDTO.getEmailTo()))
				.emailCc(String.join(MAIL_ADDRESS_SEPARATOR,mailDTO.getEmailCc()))
				.updateAt(mailDTO.getUpdateAt())
				.id(mailDTO.getMailId())
				.build();

		//message.setOutBox(addSenderFromMailDTO(mail, mailDTO));
//		mail.setRecipients(addRecipientsFromMailDTO(mail, mailDTO));
		
		return message;
	}
	
	
	private static MailDTO parseMailToMailDTO(Message mail) {
		MailDTO mailDTO = MailDTO.builder().mailId(mail.getId())
				.emailBody(mail.getEmailBody())				
				.createAt(mail.getCreateAt())
				.updateAt(mail.getUpdateAt())
//				.emailFrom(mail.getOutBox().getAddress().getAddress())
				.emailTo(new HashSet<String>(Arrays.asList(mail.getEmailTo().split(MAIL_ADDRESS_SEPARATOR))))
				.emailCc(new HashSet<String>(Arrays.asList(mail.getEmailCc().split(MAIL_ADDRESS_SEPARATOR)))).build();
//				.emailStatus(mail.getOutBox().getEmailStatus()).build();
//		addEmailAddressFromRecipients(mailDTO, mail.getRecipients());
		return mailDTO;
	}
	
	private static Set<MailDTO> parseMailToMailDTOList(Set<Message> mails) {
		Set<MailDTO> mailDTOList = new HashSet<>();
		mails.forEach(mail -> mailDTOList.add(parseMailToMailDTO(mail)));
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
	
//	private static Set<InBox> addRecipientsFromMailDTO(Message mail, MailDTO mailDTO){
//		Set<InBox> recipients = new HashSet<>();
//		recipients.addAll(getEmailAddress(mail,mailDTO.getEmailTo(), AddressTypeEnum.TO, mail.getOutBox().getEmailStatus()));
//		recipients.addAll(getEmailAddress(mail,mailDTO.getEmailCc(), AddressTypeEnum.BCC, mail.getOutBox().getEmailStatus()));
//		return recipients;
//	}
//	
//	private static Set<InBox> getEmailAddress(Message mail, Set<String> addressList, AddressTypeEnum addressTypeEnum, StatusEnum status ){
//		Set<InBox> recipients = new HashSet<>();
//			if(addressList != null && !addressList.isEmpty()) {
//			addressList.forEach(addressString-> {
//				Address address = Address.builder().address(addressString).build();
//				recipients.add(new InBox(mail, address, addressTypeEnum, status));
//			});
//		}
//		return recipients;	
//	}
	
}
