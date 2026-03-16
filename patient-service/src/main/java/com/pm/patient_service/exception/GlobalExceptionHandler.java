package com.pm.patient_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);

	}

	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<Map<String, String>> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
		// Log the exception message for debugging purposes,show in console
		log.warn("Email already exist exception: {}", ex.getMessage());
		Map<String, String> errors = new HashMap<>();
		// in postman show the message
		errors.put("message", "Email already exist");
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(PatientNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex) {
		// Log the exception message for debugging purposes,show in console
		log.warn("Patient not found exception: {}", ex.getMessage());
		Map<String, String> errors = new HashMap<>();
		// in postman show the message
		errors.put("message", "Patient not found");
		return ResponseEntity.badRequest().body(errors);
	}

}
