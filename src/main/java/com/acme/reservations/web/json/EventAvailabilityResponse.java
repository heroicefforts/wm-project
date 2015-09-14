package com.acme.reservations.web.json;

import com.acme.reservations.domain.Event;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class EventAvailabilityResponse implements Payload {
	private Event event;
	
	@JsonInclude(Include.NON_NULL)
	private Integer venueLevel;
	
	private int seatsAvailable;
	
	
	public EventAvailabilityResponse(Event event, Integer venueLevel, int seats) {
		this.event = event;
		this.venueLevel = venueLevel;
		this.seatsAvailable = seats;
	}

	public int getSeatsAvailable() {
		return seatsAvailable;
	}
	
	public void setSeatsAvailable(int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	
	public Event getEvent() {
		return event;
	}

	public Integer getVenueLevel() {
		return venueLevel;
	}
	
}
