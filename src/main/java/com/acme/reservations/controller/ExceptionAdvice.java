package com.acme.reservations.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler.
 * 
 * @author jevans
 *
 */
@ControllerAdvice
public class ExceptionAdvice extends AbstractJSONController {
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public @ResponseBody String handleError404(HttpServletResponse response)   {
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return response(404, "No such endpoint.");
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody String handleError500(HttpServletResponse response)   {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return response(500, "There was an error processing your request.  Please try again later.");
	}
	
}
