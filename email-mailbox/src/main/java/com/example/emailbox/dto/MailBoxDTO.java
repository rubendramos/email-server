package com.example.emailbox.dto;

import com.example.emailbox.modelo.enums.MailBoxType;
import com.example.emailbox.modelo.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MailBoxDTO {
	private String emailAddress;	
	private StatusEnum emailStatus;	
	private MailBoxType mailBoxType;
}
