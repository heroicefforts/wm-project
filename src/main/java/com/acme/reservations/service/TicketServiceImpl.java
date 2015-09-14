package com.acme.reservations.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.reservations.dao.ReservationDAO;
import com.acme.reservations.domain.Customer;
import com.acme.reservations.domain.Event;
import com.acme.reservations.domain.ReservationCriteria;
import com.acme.reservations.domain.SeatHold;

@Service
public class TicketServiceImpl implements TicketService {
	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
	
	private ReservationDAO dao;
	
	
	@Autowired
	public TicketServiceImpl(ReservationDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public int numSeatsAvailable(int eventId, Integer venueLevel) {
		Event event = dao.getEvent(eventId);
		return event.getAvailableSeatsForVenue(venueLevel);
	}

	@Override
	public SeatHold findAndHoldSeats(int eventId, String customerEmail, ReservationCriteria crit) {
		logger.debug("Customer<{}>:  Attempting to hold {} seats.", customerEmail, crit.getMinSeats());
		Event event = dao.getEvent(eventId);
		Customer customer = dao.findCustomerByEmail(customerEmail);
		if(customer == null)
			customer = dao.addCustomer(customerEmail);
		
		dao.lock(event);
		
		SeatHold hold = event.holdSeating(customer, crit);
		if(hold != null) {
			dao.saveEntity(event);
			dao.saveEntity(hold);
		}
		
		return hold;
	}

	@Override
	public String reserveSeats(String customerEmail, String seatHoldGuid) {
		SeatHold hold = dao.getSeatHold(customerEmail, seatHoldGuid);
		String confirmationCode = null;
		if(hold != null) {
			if(!hold.isExpired()) {
				Event event = hold.getEvent();
				dao.lock(event);
				event.reserve(hold);
				dao.saveEntity(event);
				dao.saveEntity(hold);
				
				confirmationCode = hold.getConfirmation();
			}
			else {
				logger.debug("Customer<{}>:  Hold {} has expired.", customerEmail, seatHoldGuid);
			}
		}
		else {
			logger.debug("Customer<{}>:  No such hold:  '{}'.", customerEmail, seatHoldGuid);
		}
		
		return confirmationCode;
	}

	@Override
	public Event getEvent(int eventId) {
		return dao.getEvent(eventId);
	}

}
