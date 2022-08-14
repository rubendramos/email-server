package com.example.emailbox;

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

import com.example.emailbox.entity.Email;
import com.example.emailbox.entity.OutBox;
import com.example.emailbox.exceptions.MailServiceException;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.repository.InBoxRepository;
import com.example.emailbox.repository.OutBoxRepository;
import com.example.emailbox.service.OutBoxService;
import com.example.emailbox.service.OutBoxServiceImpl;

@SpringBootTest
public class EMailBoxServiceMockTest {

	@Mock
	private OutBoxRepository outBoxRepository;
	
	@Mock
	private InBoxRepository inBoxRepository;
	
	private OutBoxService outBoxService;
	
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		outBoxService = new OutBoxServiceImpl(outBoxRepository, inBoxRepository);	
		
		Address address1 = Address.builder()
				.address("a1@gmail.com").build();
		
		Message message1 = Message.builder()
				.emailBody("Hello world")
				.emailFrom("b@domain.com")
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime())).build();

		OutBox outBoxTest1 = new OutBox(message1, address1, StatusEnum.BORRADOR);
		
		Mockito.when(outBoxRepository.findById(13L))
		.thenReturn(Optional.of(outBoxTest1));
		
		Mockito.when(outBoxRepository.save((OutBox)outBoxTest1))
		.thenReturn(outBoxTest1);
		
	}
	
	@Test
	public void whenValidGetId_thenReturnAddress() throws MailServiceException {
		Email emailFound= outBoxService.getOutBoxById(13L);
		Assertions.assertThat(emailFound.getAddress()).isEqualTo("b@gmail.com");
	}
	

}
