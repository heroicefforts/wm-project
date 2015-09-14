package com.acme.reservations.dao;

import java.io.Serializable;
import java.util.List;

import com.acme.reservations.domain.Customer;
import com.acme.reservations.domain.Event;
import com.acme.reservations.domain.SeatHold;

public interface ReservationDao {

	public abstract Event getEvent(int id);

	public abstract Customer addCustomer(String customerEmail);

	public abstract Customer findCustomerByEmail(String customerEmail);

	public abstract void saveEntity(Serializable entity);

	public abstract SeatHold getSeatHold(String customerEmail, String seatHoldGuid);

	public abstract void lock(Serializable entity);

	public abstract List<Event> findEvents(int offset, int limit);

}