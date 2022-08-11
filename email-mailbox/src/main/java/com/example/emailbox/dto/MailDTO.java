package com.example.emailbox.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
private String emailFrom;
private Set<String> emailTo;
private Set<String> emailCc;
private String emailBody;
private Date createAt;
private Date updateAt;
private StatusEnum emailStatus;

}
