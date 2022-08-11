package com.example.emailmessage.exceptions;

import java.util.Arrays;

public class BaseException extends Exception {

	/** Constant "serialVersionUID". */
	private static final long serialVersionUID = 1L;

	/** Origin exception */
	private Exception originException;

	/** Asociated params to de exception. */
	private final transient Object[] params;

	/**
	 * Instantiates a new base exception.
	 *
	 * @param exception            the exception
	 * @param parametros1            the parametros1
	 */

	public BaseException(Exception exception, Object[] paramsException) {
		super(exception.getMessage(), exception.getCause());
		if (paramsException == null) {
			this.params = new String[0];
		} else {
			this.params = Arrays.copyOf(paramsException, paramsException.length);
		}

		this.originException = exception;
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param mensaje
	 *            the mensaje
	 * @param parametros1
	 *            the parametros1
	 */

	public BaseException(String message, Object[] paramsException) {
		super(message);
		if (paramsException == null) {
			this.params = new String[0];
		} else {
			this.params = Arrays.copyOf(paramsException, paramsException.length);
		}

		this.originException = this;
	}

	/**
	 * Gets the exception origen.
	 *
	 * @return the exception origen
	 */
	public Exception getOriginException() {
		return this.originException;
	}

	/**
	 * Sets the exception origen.
	 *
	 * @param exceptionOrigen
	 *            the new exception origen
	 */

	/**
	 * Gets the parametros.
	 *
	 * @return the parametros
	 */
	public Object[] getParametros() {

		if (this.params != null) {
			return this.params.clone();
		}

		return new Object[0];
	}

	
}
