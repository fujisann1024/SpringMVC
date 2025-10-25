package com.sample.mvcApp.common.exception;

public class DomainObjectException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DomainObjectException(String message) {
		super(message);
	}
	
	public DomainObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
