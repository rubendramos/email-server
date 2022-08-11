package com.example.emailbox.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailApiServerError {

	private HttpStatus status;
	private String message;
	private List<String> errors;
	
	
	  public EmailApiServerError() {
	        super();
	    }

	    public EmailApiServerError(final HttpStatus status, final String message, final List<String> errors) {
	        super();
	        this.status = status;
	        this.message = message;
	        this.errors = errors;
	    }

	    public EmailApiServerError(final HttpStatus status, final String message, final String error) {
	        super();
	        this.status = status;
	        this.message = message;
	        errors = Arrays.asList(error);
	    }
	
}
