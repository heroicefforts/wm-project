package com.acme.reservations.service;

import com.acme.reservations.domain.Event;
import com.acme.reservations.domain.ReservationCriteria;
import com.acme.reservations.domain.SeatHold;


public interface TicketService {
	/**
	 * The number of seats in the requested level that are neither held nor reserved
	 *
	 * @param eventId the event identifier
	 * @param venueLevel a numeric venue level identifier to limit the search
	 * @return the number of tickets available on the provided level
	 */
	public int numSeatsAvailable(int eventId, Integer venueLevel);

	
	/**
	 * Find and hold the best available seats for a customer
	 *
	 * @param eventId the event identifier
	 * @param crit the criteria that is required to match before establishing a hold
	 * @param customerEmail unique identifier for the customer
	 * @return a SeatHold object identifying the specific seats and related	information
	 */
	public SeatHold findAndHoldSeats(int eventId, String customerEmail, ReservationCriteria crit);
	
	/**
	* Commit seats held for a specific customer
	*
	* @param customerEmail the email address of the customer to which the seat hold is assigned
	* @param seatHoldGuid the seat hold identifier
	* @return a reservation confirmation code
	*/
	public String reserveSeats(String customerEmail, String seatHoldGuid);


	public Event getEvent(int eventId);

}
