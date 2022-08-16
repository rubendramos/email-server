package com.example.emailbox.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.emailbox.modelo.Address;

@Component
public class AddresssHystrixFallBackFactory implements AddressClient {

	@Override
	public ResponseEntity<Address> getAddressByEmailAddress(String emailAddress) {
		return  ResponseEntity.ok(getUnknownAddress());
	}

	@Override
	public ResponseEntity<Address> getAddressById(Long addressId) {
		return  ResponseEntity.ok(getUnknownAddress());	}
	
	/**
	 * Retrieves a unknown address
	 * @return
	 */
	private Address getUnknownAddress() {
		return  Address.builder().address("Unknown").build();
	}

}
