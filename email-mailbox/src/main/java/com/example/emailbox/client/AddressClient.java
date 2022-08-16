package com.example.emailbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.emailbox.modelo.Address;

@FeignClient(name ="email-address",path="/api/address",fallback = AddresssHystrixFallBackFactory.class)
public interface AddressClient {
	
	/**
	 * Get a {@link Address} by string email address
	 * @param emailAddress
	 * @return
	 */
	@GetMapping(value = "/{emailAddress}")
	public ResponseEntity<Address> getAddressByEmailAddress(@PathVariable("emailAddress") String emailAddress);
	
	/**
	 * Get a {@link Address} by Id
	 * @param addressId
	 * @return
	 */
	@GetMapping(value = "/addressId/{addressId}")
	public ResponseEntity<Address> getAddressById(@PathVariable("addressId") Long addressId);

}
