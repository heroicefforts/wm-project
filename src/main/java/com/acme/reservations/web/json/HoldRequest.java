package com.acme.reservations.web.json;

import org.apache.commons.lang3.StringUtils;

import com.acme.reservations.domain.ReservationCriteria;


public class HoldRequest implements Request {
	private String customerEmail;
	private int eventId = -1;
	private ReservationCriteria criteria;
	
	
	public String getCustomerEmail() {
		return customerEmail;
	}
	
	public void setCustomerEmail(String customer) {
		this.customerEmail = customer;
	}
	
	public int getEventId() {
		return eventId;
	}
	
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public ReservationCriteria getCriteria() {
		return criteria;
	}
	
	public void setCriteria(ReservationCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public boolean isValid() {
		return StringUtils.isNotBlank(customerEmail) && eventId > -1 && criteria.isValid();
	}
	
}
