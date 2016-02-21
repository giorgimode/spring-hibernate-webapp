package com.giorgi.spring.web.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(DataAccessException.class)
	public String databaseErrorHandler(DataAccessException ex) {

		ex.printStackTrace();
		return "error";
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public String accessErrorHandler(AccessDeniedException ex) {
		return "denied";
	}
}
