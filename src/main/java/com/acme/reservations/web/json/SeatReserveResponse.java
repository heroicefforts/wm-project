package com.acme.reservations.web.json;


public class SeatReserveResponse implements Payload {
	private String confirmationId;
	
	public SeatReserveResponse(String confirmationId) {
		this.confirmationId = confirmationId;
	}
	
	public String getConfirmationId() {
		return confirmationId;
	}
	
	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}
	
}
