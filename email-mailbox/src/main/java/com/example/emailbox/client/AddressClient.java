package com.example.emailbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.emailbox.modelo.Address;

@FeignClient(name ="email-address",path="/api/address")
public interface AddressClient {
	
	@GetMapping(value = "/{emailAddress}")
	public ResponseEntity<Address> getAddressByEmailAddress(@PathVariable("emailAddress") String emailAddress);
	
	@GetMapping(value = "/addressId/{addressId}")
	public ResponseEntity<Address> getAddressById(@PathVariable("addressId") Long addressId);

}
