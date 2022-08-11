package com.example.emailaddress.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailaddress.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

	/**
	 * Retrieves adrress entity from string address
	 * @param addrress
	 * @return
	 */
	public Address findByAddress(String addrress);
	
	/**
	 *  Retrieves adrress entity from id 
	 */
	public Optional<Address>  findById(Long id);
}
