package com.pm.patient_service.exception;


public class EmailAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistException(String message) {
		super(message);
	}

}
