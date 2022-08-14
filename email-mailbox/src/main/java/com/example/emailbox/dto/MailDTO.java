package com.example.emailbox.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.example.emailbox.modelo.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MailDTO implements Serializable{


private static final long serialVersionUID = 371198328505788622L;
	
private Long mailId;

@NotEmpty(message = "From email address is mandatory")
@Email (message = "From email address format is not valid")
private String emailFrom;

@NotEmpty(message = "To email address is mandatory")
private Set<@NotBlank @Email(message = "To email address format is not valid") String> emailTo;

private Set<@NotBlank @Email(message = "Cc email address format is not valid") String> emailCc;

private String emailBody;

private Date createAt;

private Date updateAt;

private StatusEnum emailStatus;

}
