package com.example.emailbox;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.emailbox.entity.OutBox;
import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;
import com.example.emailbox.repository.InBoxRepository;
import com.example.emailbox.repository.OutBoxRepository;




@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EMailBoxRepositoryTest {
	
	@Autowired
	private InBoxRepository inRepository;

	
	@Autowired
	private OutBoxRepository outRepository;

	private static final int NUMBER_MAILS_FROMA_A = 4;

	@Test
	public void whenFindOutBoxEMail_thenReturnsOutBoxEMail() {
		
		
		Address addres1 = Address.builder()
				.address("a1@gmail.com").build();
		
		Message testMail1 = Message.builder()
				.emailBody("Hello world")
				.emailFrom("b@domain.com")
				.emailTo("a@domain.com")
				.createAt(new Timestamp(new Date().getTime())).build();

		OutBox outBox = new OutBox(testMail1, addres1, StatusEnum.BORRADOR);
				
		
		outRepository.save(outBox);

		
		List<OutBox>  outBoxList = outRepository.findAll();
		
		Assertions.assertThat(outBoxList.size()).isEqualTo(NUMBER_MAILS_FROMA_A);

	}
}
