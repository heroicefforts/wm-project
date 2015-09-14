package com.acme.reservations.exception;

import com.acme.reservations.domain.SeatHold;


/**
 * Thrown if an operation is unexpectly attempted on an expired SeatHold.
 * 
 * @author jevans
 *
 */
public class SeatHoldExpiredException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private SeatHold hold;
	
	
	public SeatHoldExpiredException(SeatHold hold) {
		super("Seat hold '" + hold.getGuid() + "' has expired.");
		this.hold = hold;
	}
	
	public SeatHold getHold() {
		return hold;
	}
	
}
