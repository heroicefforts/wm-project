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
public class ReservationDaoImpl implements ReservationDao {
			
	private SessionFactory factory;
	
	
	@Autowired
	public ReservationDaoImpl(SessionFactory factory) {
		this.factory = factory;
	}
	
	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#getEvent(int)
	 */
	@Override
	public Event getEvent(int id) {
		return (Event) factory.getCurrentSession().get(Event.class, id);
	}

	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#addCustomer(java.lang.String)
	 */
	@Override
	public Customer addCustomer(String customerEmail) {
		Customer customer = new Customer();
		customer.setEmail(customerEmail);
		factory.getCurrentSession().save(customer);
		return customer;
	}
	
	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#findCustomerByEmail(java.lang.String)
	 */
	@Override
	public Customer findCustomerByEmail(String customerEmail) {
		return (Customer) factory.getCurrentSession().createQuery("from " + Customer.class.getName() + " where email = :email")
			.setString("email", customerEmail)
			.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#saveEntity(java.io.Serializable)
	 */
	@Override
	public void saveEntity(Serializable entity) {
		factory.getCurrentSession().save(entity);
	}

	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#getSeatHold(java.lang.String, java.lang.String)
	 */
	@Override
	public SeatHold getSeatHold(String customerEmail, String seatHoldGuid) {
		Customer customer = findCustomerByEmail(customerEmail);
		return (SeatHold) factory.getCurrentSession().createQuery("from " + SeatHold.class.getName() + 
				" where customer = :customer and guid = :guid")
			.setEntity("customer", customer)
			.setString("guid", seatHoldGuid)
			.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#lock(java.io.Serializable)
	 */
	@Override
	public void lock(Serializable entity) {
		factory.getCurrentSession().buildLockRequest(new LockOptions().setLockMode(LockMode.PESSIMISTIC_WRITE).setTimeOut(10000)).lock(entity);
	}

	/* (non-Javadoc)
	 * @see com.acme.reservations.dao.ReservationDao#findEvents(int, int)
	 */
	@Override
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
