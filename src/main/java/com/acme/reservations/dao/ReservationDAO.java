package com.acme.reservations.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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

	public Customer addCustomer(String customerEmail) {
		Customer customer = new Customer();
		customer.setEmail(customerEmail);
		factory.getCurrentSession().save(customer);
		return customer;
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

	@SuppressWarnings("unchecked")
	public List<Event> findEvents(int offset, int limit) {
		return factory.getCurrentSession().createCriteria(Event.class)
			.addOrder(Order.asc("startDttm"))
			.setCacheable(true)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.list();
	}
	
}
