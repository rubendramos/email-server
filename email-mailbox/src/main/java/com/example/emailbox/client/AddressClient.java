package com.example.emailbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.emailbox.modelo.Address;

@FeignClient(name ="address-service")
@RequestMapping(value = "/emailAddress")
public interface AddressClient {
	
	@GetMapping(value = "/{emailAddress}")
	public ResponseEntity<Address> getAddressByEmailAddress(@PathVariable("emailAddress") String emailAddress);

}
