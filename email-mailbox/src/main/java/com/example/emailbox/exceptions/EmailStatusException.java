package com.example.emailbox.exceptions;

public class EmailStatusException extends MailServiceException{


	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "El estatus del email es incorrecto para la operación a procesar";

	public EmailStatusException(Exception exception, Object[] paramsException) {
		super(exception, paramsException);
	}
	
	
	public EmailStatusException(String message, Object[] paramsException) {
		super(message, paramsException);
	}
	
	
	public EmailStatusException( Object[] paramsException) {
		super(MESSAGE, paramsException);
	}

}
