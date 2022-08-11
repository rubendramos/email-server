package com.example.emailmessage.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable{

	private static final long serialVersionUID = 1205296014260490530L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
//	@OneToOne
//	@JoinColumn(name="message_id")
//	@PrimaryKeyJoinColumn
//	private OutBox outBox;
//    
//	
//	@OneToMany(mappedBy = "message")
//	private Set<InBox> recipients;	
	
	@Column(name = "email_from")
	private String emailFrom;	
	
	@Column(name = "email_to")
	private String emailTo;

	@Column(name = "email_cc")
	private String emailCc;

	
	@Column(name = "email_body")
	private String emailBody;

	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;

	@Column(name = "update_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateAt;
	

//	  /**
//     * Add an Address Set to TO,CC or BCC List depends on AddressTypeEnum
//     * @param addressSet
//     * @param addressTypeEnum
//     */
//    public void addEmailAddres(Set<Address> addressSet, AddressTypeEnum addressTypeEnum, StatusEnum status) {
//    	addressSet.forEach(address -> this.addEmailAddressToRecipients(address,addressTypeEnum, status));
//    }
//    
//    /**
//     * Add an address to TO,CC or BCC List depends on AddressTypeEnum
//     * @param address
//     */
//    public void addEmailAddressToRecipients(Address address, AddressTypeEnum addressTypeEnum, StatusEnum status) {
//    	InBox emailAddress = new InBox(this, address, addressTypeEnum, status);
//    	recipients.add(emailAddress);
////    	address.getEmailAddress().add(emailAddress);
//    }
//    
//    public void addSenderToOutBox(Address address,StatusEnum status) {
//    	OutBox outbox = new OutBox(this, address, status);
//    	this.outBox = outbox;
//    	
//    }
     
}
