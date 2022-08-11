package com.example.emailbox.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultipleEmailDTO {
	
	private Long multipleMailId;
	private Set<MailDTO> emailsDTOs;
	
	
}
