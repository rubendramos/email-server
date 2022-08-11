package com.example.emailbox.modelo.enums;

import java.util.stream.Stream;

public enum AddressTypeEnum {
	TO(1), CC(2), BCC(3);
	
	private int addressTypeId;

	private AddressTypeEnum(int addressTypeId) {
		this.addressTypeId = addressTypeId;
	}


	public int getAddressTypeId() {
		return this.addressTypeId;
	}

	public static AddressTypeEnum of(int addressTypeId) {
		return Stream.of(AddressTypeEnum.values()).filter(p -> p.getAddressTypeId() == addressTypeId).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
