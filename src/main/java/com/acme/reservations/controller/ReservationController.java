package com.acme.reservations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acme.reservations.domain.Event;
import com.acme.reservations.domain.SeatHold;
import com.acme.reservations.service.TicketService;
import com.acme.reservations.web.json.EventAvailabilityResponse;
import com.acme.reservations.web.json.HoldRequest;
import com.acme.reservations.web.json.ReserveRequest;
import com.acme.reservations.web.json.SeatHoldResponse;
import com.acme.reservations.web.json.SeatReserveResponse;
import com.acme.reservations.web.json.View;

/**
 * Controller providing a JSON interface to the "ticket" service.
 * 
 * @author jevans
 *
 */
@Controller
@RequestMapping("/service")
public class ReservationController extends AbstractJSONController {

	private TicketService ticketSvc;
	
	
	@Autowired
	public ReservationController(TicketService ticketSvc) {
		this.ticketSvc = ticketSvc;
	}
	
	/**
	 * Query the overall venue availability for an event.
	 * 
	 * @param eventId the id of the event.
	 * @return the event availability response.
	 */
    @RequestMapping(value="/availability/event/{eventId}")
	public @ResponseBody String availability(@PathVariable int eventId) {
    	return availability(eventId, null);
	}
    
	/**
	 * Query a venue's level specific availability for an event.
	 * 
	 * @param eventId the id of the event.
	 * @return the event availability response.
	 */
    @RequestMapping(value="/availability/event/{eventId}/level/{venueLevel}")
	public @ResponseBody String availability(@PathVariable int eventId, @PathVariable Integer venueLevel) {
    	Event event = ticketSvc.getEvent(eventId);
    	if(event != null) 
    		return response(new EventAvailabilityResponse(event, venueLevel, ticketSvc.numSeatsAvailable(eventId, venueLevel)), View.Summary.class);
    	else
    		return response(404, "No event with id:" + eventId);
	}

    /**
     * Attempts to issue a seat hold matching the given criteria for the specified event.
     * 
     * @param eventId the id of the event.
     * @param request the hold request
     * @return the seat hold response, if a hold was successfully completed.
     */
    @RequestMapping(value="/hold/event/{eventId}", method=RequestMethod.POST, headers = {"content-type=application/json"})
    public @ResponseBody String hold(@PathVariable int eventId, @RequestBody HoldRequest request) {
    	if(request.isValid()) {
	    	SeatHold hold = ticketSvc.findAndHoldSeats(request.getEventId(), request.getCustomerEmail(), request.getCriteria());
	    	if(hold != null)
	    		return response(new SeatHoldResponse(hold));
	    	else
	    		return response(1, "Could not fulfill the request with the given criteria.");
    	}
    	else
    		return invalidRequest(request);
    }

    /**
     * Attempts to reserve a previously issued seat hold.
     * 
     * @param eventId the id of the event
     * @param request the reserve request
     * @return a seat reserve response, if the hold exists and has not expired.
     */
    @RequestMapping(value="/reserve/event/{eventId}", method=RequestMethod.POST, headers = {"content-type=application/json"})
    public @ResponseBody String hold(@PathVariable int eventId, @RequestBody ReserveRequest request) {
    	if(request.isValid()) {
	    	String confirmation = ticketSvc.reserveSeats(request.getCustomerEmail(), request.getHoldId());
	    	if(confirmation != null)
	    		return response(new SeatReserveResponse(confirmation));
	    	else
	    		return response(404, "No such hold '" + request.getHoldId() + "', or hold has expired.");
    	}
    	else
    		return invalidRequest(request);
    }
    
}
