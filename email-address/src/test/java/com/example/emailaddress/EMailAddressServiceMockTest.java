package com.example.emailaddress;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.emailaddress.entity.Address;
import com.example.emailaddress.repository.AddressRepository;
import com.example.emailaddress.service.AddressService;
import com.example.emailaddress.service.AddressServiceImpl;

@SpringBootTest
public class EMailAddressServiceMockTest {

	@Mock
	private AddressRepository addresRepository;
	
	private AddressService addressService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		addressService = new AddressServiceImpl(addresRepository);	
		
		Address testAddress2 = Address.builder()
				.address("b@gmail.com").build();
		
		Mockito.when(addresRepository.findById(2L))
		.thenReturn(Optional.of(testAddress2));
		
		Mockito.when(addresRepository.save(testAddress2))
		.thenReturn(testAddress2);
		
	}
	
	@Test
	public void whenValidGetId_thenReturnAddress() {
		Address addressFound= addressService.getAddresById(2L);
		Assertions.assertThat(addressFound.getAddress()).isEqualTo("b@gmail.com");
	}
	

}
