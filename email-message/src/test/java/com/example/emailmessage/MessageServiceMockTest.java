package com.example.emailmessage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.emailmessage.entity.Message;
import com.example.emailmessage.repository.MessageRepository;
import com.example.emailmessage.service.MessageService;
import com.example.emailmessage.service.MessageServiceImpl;

@SpringBootTest
public class MessageServiceMockTest {

	@Mock
	private MessageRepository messageRepository;
	
//	@Mock
//	private AddressRepository addressRepository;
//	
//	@Mock
//	private OutBoxRepository outBoxRepository;
//	
//	@Mock
//	private InBoxRepository inBoxRepository;
//	
	private MessageService messageService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		messageService = new MessageServiceImpl(messageRepository);	
		
		Message testMessage1 = Message.builder()
				.emailBody("Hello world")
				.emailFrom("a@domain.com")				
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime())).build();
		
		Mockito.when(messageRepository.findById(1L))
		.thenReturn(Optional.of(testMessage1));
		
		Mockito.when(messageRepository.save(testMessage1))
		.thenReturn(testMessage1);
	}
	
	@Test
	public void whenValidGetId_thenReturnMail() {
		Message foundMail = messageService.getMessageById(1L);
		Assertions.assertThat(foundMail.getEmailBody()).isEqualTo("Hello world");
	}
	
}
