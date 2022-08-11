package com.example.emailmessage.exceptions;

public class EmailMessageServiceException extends BaseException{


	private static final long serialVersionUID = 3279279918225869344L;
	
	public EmailMessageServiceException(Exception exception, Object[] paramsException) {
		super(exception, paramsException);		
	}
	
	
	public EmailMessageServiceException(String message, Object[] paramsException) {
		super(message, paramsException);		
	}

}
