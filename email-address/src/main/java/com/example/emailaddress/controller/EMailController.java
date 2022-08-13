package com.example.emailaddress.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailaddress.entity.Address;
import com.example.emailaddress.service.AddressService;

@RestController
@RequestMapping(value = "api/address")
public class EMailController {

	Logger logger = LoggerFactory.getLogger(EMailController.class);

	@Autowired
	private AddressService addressService;

	@GetMapping(value = "/addressId/{addressId}")
	public ResponseEntity<Address> getAddressById(@PathVariable("addressId") Long addressId) {
		Address address = addressService.getAddresById(addressId);
		if (address != null) {
			return ResponseEntity.ok(address);
		}
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping(value = "/{emailAddress}")
	public ResponseEntity<Address> getAddressByEmailAddress(@PathVariable("emailAddress") String emailAddress) {
		Address address = addressService.getAddresFromAddresString(emailAddress);
		if (address != null) {
			return ResponseEntity.ok(address);
		}
		return ResponseEntity.noContent().build();
	}

}
