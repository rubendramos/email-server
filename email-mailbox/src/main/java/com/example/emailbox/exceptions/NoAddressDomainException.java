package com.example.emailbox.exceptions;

public class NoAddressDomainException extends MailServiceException{


	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_EXCEPTION = "Email adrress not belongs to current domain";

	public NoAddressDomainException(Exception exception, Object[] paramsException) {
		super(exception, paramsException);
	}
	
	
	public NoAddressDomainException(String message, Object[] paramsException) {
		super(message, paramsException);
	}
	
	
	public NoAddressDomainException(Object[] paramsException) {
		super(MESSAGE_EXCEPTION, paramsException);
	}

}
