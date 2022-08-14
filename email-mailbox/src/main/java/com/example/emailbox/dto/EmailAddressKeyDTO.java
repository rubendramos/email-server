package com.example.emailbox.dto;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class EmailAddressKeyDTO implements Serializable{

	private static final long serialVersionUID = -8962284758773822961L;

	public EmailAddressKeyDTO(){}
	
	@NotEmpty(message = "MessageId is mandatory")
    Long messageId;

    @NotEmpty(message = "AddressId is mandatory")
    Long addressId;
	
	
}
