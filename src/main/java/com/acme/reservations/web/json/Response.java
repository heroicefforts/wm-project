package com.acme.reservations.web.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class Response {
	
	private int status;
	
	@JsonInclude(Include.NON_NULL)
	private String errorMsg;
	
	private Date time = new Date();

	@JsonInclude(Include.NON_NULL)
	private Request badRequest;

	@JsonInclude(Include.NON_NULL)
	private Object data;


	
	public Response(Payload data) {
		this.data = data;
	}
	
	public Response(int status, String errorMsg) {
		this.status = status;
		this.errorMsg = errorMsg;
	}

	public Response(int status, String errorMsg, Request request) {
		this.status = status;
		this.errorMsg = errorMsg;
		this.badRequest = request;
	}

	public Date getTime() {
		return time;
	}

	public int getStatus() {
		return status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public Request getBadRequest() {
		return badRequest;
	}

	public Object getData() {
		return data;
	}
	
}
