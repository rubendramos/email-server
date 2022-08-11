package com.example.emailservice.domain;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Mail implements Serializable {

	private static final long serialVersionUID = 3281307086374406505L;

	private String emailFrom;
	private Set<String> emailTo;
	private Set<String> emailCc;
	private String emailBody;

}
