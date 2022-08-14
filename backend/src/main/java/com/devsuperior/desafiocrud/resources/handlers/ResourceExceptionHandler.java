package com.devsuperior.desafiocrud.resources.handlers;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.desafiocrud.dto.StandardError;
import com.devsuperior.desafiocrud.dto.ValidationError;
import com.devsuperior.desafiocrud.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	private static final String RESOURCE_NOT_FOUND = "Resource not found";
	private static final String INVALID_DATA = "Invalid input data";

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), RESOURCE_NOT_FOUND, e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e,
			HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError(Instant.now(), status.value(), INVALID_DATA, "",
				request.getRequestURI());
		e.getBindingResult().getFieldErrors().forEach(err1 -> err.addError(err1.getField(), err1.getDefaultMessage()));

		return ResponseEntity.status(status).body(err);
	}
}
