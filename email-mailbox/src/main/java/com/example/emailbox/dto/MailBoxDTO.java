package com.example.emailbox.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.example.emailbox.modelo.enums.MailBoxType;
import com.example.emailbox.modelo.enums.StatusEnum;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MailBoxDTO {
	
	@NotEmpty(message = "Email Address is mandatory")
	@Email (message = "Email address format is not valid")
	private String emailAddress;
	
	private StatusEnum emailStatus;	
	
	private MailBoxType mailBoxType;
}
