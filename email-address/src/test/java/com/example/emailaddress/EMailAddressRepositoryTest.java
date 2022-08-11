package com.example.emailaddress;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.emailaddress.entity.Address;
import com.example.emailaddress.repository.AddressRepository;




@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EMailAddressRepositoryTest {
	
	@Autowired
	private AddressRepository addressRepository;
	
	private static final int NUMBER_MAILS_FROMA_A = 4;

	@Test
	public void whenFindByAddress_thenReturnsAEmailAddress() {
		
		
		Address testAddress1 = Address.builder()
				.address("a1@gmail.com").build();

		
		addressRepository.save(testAddress1);

		
		List<Address> addressList = addressRepository.findAll();
		
		Assertions.assertThat(addressList.size()).isEqualTo(NUMBER_MAILS_FROMA_A);

	}
}
