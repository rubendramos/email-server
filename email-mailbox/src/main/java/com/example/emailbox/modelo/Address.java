package com.example.emailbox.modelo;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address implements Serializable {


	private static final long serialVersionUID = 8266511525375268860L;

    private Long id;
	
    private String address;
    
    	
}
