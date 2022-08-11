package com.example.emailbox.exceptions;

public class MailServiceException extends BaseException{


	private static final long serialVersionUID = 3279279918225869344L;
	
	public MailServiceException(Exception exception, Object[] paramsException) {
		super(exception, paramsException);		
	}
	
	
	public MailServiceException(String message, Object[] paramsException) {
		super(message, paramsException);		
	}

}
