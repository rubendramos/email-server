package com.example.emailmessage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.emailmessage.entity.Message;
import com.example.emailmessage.repository.MessageRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MessageRepositoryTest {
	
	@Autowired
	private MessageRepository messageRepository;
	
	private static final int NUMBER_MAILS_FROMA_A = 2;

	@Test
	public void whenFindBySender_thenReturnsListOfMails() {
		
		
		Message testMail1 = Message.builder()
				.emailBody("Hello world")
				.emailFrom("b@domain.com")
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime())).build();
		
		messageRepository.save(testMail1);
		
		List<Message> mailListFrom = messageRepository.findByEmailFrom(testMail1.getEmailFrom());
		
		Assertions.assertThat(mailListFrom.size()).isEqualTo(NUMBER_MAILS_FROMA_A);

	}
}


