package com.example.emailmessage.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
	
	@NotEmpty(message = "From email address is mandatory")
	@Email (message = "From email address format is not valid")
	@Column(name = "email_from")
	private String emailFrom;	
	
	@NotEmpty(message = "To email address is mandatory")
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

}
