package com.example.emailbox.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.emailbox.modelo.Address;
import com.example.emailbox.modelo.Message;
import com.example.emailbox.modelo.enums.StatusEnum;

@Entity
@Table(name = "tbl_out_box")
public class OutBox implements Serializable,Email {

	private static final long serialVersionUID = 4301696990442367147L;

	@Id
    @Column(name = "message_id")
    private Long id;
	
//    @OneToOne(cascade = CascadeType.ALL)
//    @MapsId
//    @JoinColumn(name="message_id")
    private Message message;
	
//    @OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "address_id")
	Address address;

	
	public OutBox() {
	}

	public OutBox(Message message, Address address, StatusEnum status) {
		this.message = message;
		this.address = address;
		this.emailStatus =status;
	}
	

	@Transient
	private StatusEnum emailStatus;

	@Column(name = "email_status")
	private int emailStatusValue;


	@PostLoad
	void fillTransient() {
		if (emailStatusValue > 0) {
			this.emailStatus = StatusEnum.of(emailStatusValue);
		}
		
	}

	@PrePersist
	void fillPersistent() {
		if (this.emailStatus != null) {
			this.emailStatusValue = this.emailStatus.getStatusId();
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

	public Long getId() {
		return id;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message mail) {
		this.message = mail;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address addressId) {
		this.address = addressId;
	}


}
