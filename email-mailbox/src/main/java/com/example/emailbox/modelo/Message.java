package com.example.emailbox.modelo;


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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable{

	private static final long serialVersionUID = 1205296014260490530L;

	private Long id;
	
	private String emailFrom;	
	
	private String emailTo;

	private String emailCc;
	
	private String emailBody;

	private Date createAt;

	private Date updateAt;
	
     
}
