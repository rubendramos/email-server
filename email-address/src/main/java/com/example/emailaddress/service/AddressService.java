package com.example.emailaddress.service;

import java.util.List;

import com.example.emailaddress.entity.Address;

public interface AddressService {

	
	/**
	 * Retrieves all mails address list
	 * @return
	 */
	public List<Address> listAllAddress();
	
	/**
	 * Retrieves mail address by address string
	 * @return
	 */
	public Address getAddresFromAddresString(String addresss);
	
	/**
	 * Retrieves mail address by ID
	 * @return
	 */
	public Address getAddresById(Long id);
	
}
