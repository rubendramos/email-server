package com.example.emailbox.modelo;


import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
	
	@NotEmpty(message = "From email address is mandatory")
	@Email (message = "From email address format is not valid")
	private String emailFrom;	
	
	@NotEmpty(message = "To email address is mandatory")
	private String emailTo;

	private String emailCc;
	
	private String emailBody;

	private Date createAt;

	private Date updateAt;
	
     
}
