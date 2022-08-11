package com.example.emailbox.modelo.enums;

import java.util.stream.Stream;


public enum StatusEnum {

	ENVIADO(1), BORRADOR(2), ELIMINADO(3), SPAN(4);

	private int statusId;

	private StatusEnum(int statusId) {
		this.statusId = statusId;
	}


	public int getStatusId() {
		return this.statusId;
	}

	public static StatusEnum of(int status) {
		return Stream.of(StatusEnum.values()).filter(p -> p.getStatusId() == status).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
