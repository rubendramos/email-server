package com.example.emailaddress.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.emailaddress.entity.Address;
import com.example.emailaddress.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepositoty;

	@Override
	public List<Address> listAllAddress() {
		return addressRepositoty.findAll();
	}

	@Override
	public Address getAddresFromAddresString(String addresss) {
		return addressRepositoty.findByAddress(addresss);
	}

	@Override
	public Address getAddresById(Long id) {
		return  addressRepositoty.findById(id).get();
	}

	

	
}
