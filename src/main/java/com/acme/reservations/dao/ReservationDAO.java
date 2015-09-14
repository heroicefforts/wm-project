package com.acme.reservations.dao;

import java.io.Serializable;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.acme.reservations.domain.Customer;
import com.acme.reservations.domain.Event;
import com.acme.reservations.domain.SeatHold;

@Repository("reservationDao")
public class ReservationDAO {
			
	private SessionFactory factory;
	
	
	@Autowired
	public ReservationDAO(SessionFactory factory) {
		this.factory = factory;
	}
	
	public Event getEvent(int id) {
		return (Event) factory.getCurrentSession().get(Event.class, id);
	}

	public Customer findCustomerByEmail(String customerEmail) {
		return (Customer) factory.getCurrentSession().createQuery("from " + Customer.class.getName() + " where email = :email")
			.setString("email", customerEmail)
			.uniqueResult();
	}

	public void saveEntity(Serializable entity) {
		factory.getCurrentSession().save(entity);
	}

	public SeatHold getSeatHold(String customerEmail, String seatHoldGuid) {
		Customer customer = findCustomerByEmail(customerEmail);
		return (SeatHold) factory.getCurrentSession().createQuery("from " + SeatHold.class.getName() + 
				" where customer = :customer and guid = :guid")
			.setEntity("customer", customer)
			.setString("guid", seatHoldGuid)
			.uniqueResult();
	}

	public void lock(Serializable entity) {
		factory.getCurrentSession().buildLockRequest(new LockOptions().setLockMode(LockMode.PESSIMISTIC_WRITE).setTimeOut(10000)).lock(entity);
	}
	
}
