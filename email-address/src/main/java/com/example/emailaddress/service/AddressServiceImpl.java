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
	
	/*
	 * (non-Javadoc)
	 * @see com.example.emailaddress.service.AddressService#createAddress(com.example.emailaddress.entity.Address)
	 */
	@Override
	public Address createAddress(Address address) {
		return addressRepositoty.save(address);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.example.emailaddress.service.AddressService#listAllAddress()
	 */
	@Override
	public List<Address> listAllAddress() {
		return addressRepositoty.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.emailaddress.service.AddressService#getAddresFromAddresString(java.lang.String)
	 */
	@Override
	public Address getAddresFromAddresString(String addresss) {
		return addressRepositoty.findByAddress(addresss);
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.emailaddress.service.AddressService#getAddresById(java.lang.Long)
	 */
	@Override
	public Address getAddresById(Long id) {
		return  addressRepositoty.findById(id).get();
	}

	

	
}
