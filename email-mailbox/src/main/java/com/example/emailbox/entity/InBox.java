package com.example.emailbox.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.AddressTypeEnum;
import com.example.emailbox.modelo.enums.StatusEnum;

@Entity
@Table(name = "tbl_in_box")
public class InBox implements Serializable,Email {

	private static final long serialVersionUID = 5378871332096374447L;

	@EmbeddedId
	EmailAddressKey id;

//	@ManyToOne
//	@MapsId("messageId")
//	@Column(name = "message_id")
	@Transient
	Message message;

//	@ManyToOne
//	@MapsId("addressId")
//	@Column(name = "address_id")
	@Transient
	Address address;

	public EmailAddressKey getId() {
		return id;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public AddressTypeEnum getEmailAddressType() {
		return emailAddressType;
	}

	public void setEmailAddressType(AddressTypeEnum emailAddressType) {
		this.emailAddressType = emailAddressType;
	}

	public int getEmailAddressTypeValue() {
		return emailAddressTypeValue;
	}

	public void setEmailAddressTypeValue(int emailAddressTypeValue) {
		this.emailAddressTypeValue = emailAddressTypeValue;
	}

	@Transient
	private StatusEnum emailStatus;

	@Column(name = "email_status")
	private int emailStatusValue;

	@Transient
	private AddressTypeEnum emailAddressType;

	@Column(name = "address_type")
	private int emailAddressTypeValue;

	@PostLoad
	void fillTransient() {
		if (emailStatusValue > 0) {
			this.emailStatus = StatusEnum.of(emailStatusValue);
		}
		
		if (emailAddressTypeValue > 0) {
			this.emailAddressType = AddressTypeEnum.of(emailAddressTypeValue);
		}
	}

	@PrePersist
	void fillPersistent() {
		if (this.emailStatus != null) {
			this.emailStatusValue = this.emailStatus.getStatusId();
		}
		
		if (emailAddressType != null) {
			this.emailAddressTypeValue = emailAddressType.getAddressTypeId();
		}

		
	}
	
	
	public StatusEnum getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(StatusEnum emailStatus) {
		this.emailStatus = emailStatus;
	}

	public int getEmailStatusValue() {
		return emailStatusValue;
	}

	public void setEmailStatusValue(int emailStatusValue) {
		this.emailStatusValue = emailStatusValue;
	}
	

	public InBox() {
	}

	public InBox(Message message, Address address, AddressTypeEnum emailAddressType, StatusEnum status) {
		this.message = message;
		this.address = address;
		this.emailAddressType = emailAddressType;
		this.emailStatus =status;
		this.emailAddressTypeValue = this.emailAddressType.getAddressTypeId();
		this.emailStatusValue = this.emailStatus.getStatusId();
		this.id = new EmailAddressKey(this.message.getId(), address.getId());
	}



}
