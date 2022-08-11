package com.example.emailbox.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class EmailAddressKey implements Serializable{

	private static final long serialVersionUID = -8962284758773822961L;

	public EmailAddressKey(){}
	
	@Column(name = "message_id")
    Long messageId;

    @Column(name = "address_id")
    Long addressId;
	
	
}
