package com.example.emailbox.dto;

import java.util.Set;

import com.example.emailbox.modelo.enums.MailBoxType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultipleDeleteDTO {
	
	private Long mailId;
	private Long addressId;
	private MailBoxType mailBoxType;
	private Set<Long> emailIDs;
	
	
}
