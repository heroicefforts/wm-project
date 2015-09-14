package com.acme.reservations.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Domain object representing the lease of a group of seats within one level of a venue for a given event.
 * 
 * @author jevans
 *
 */
public class SeatLease implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private EventSeating seating;
	private Customer customer;
	private Date expiration;
	private int seats;

	
	public SeatLease(EventSeating eventSeating, Customer customer, Date expiration, int seats) {
		this.seating = eventSeating;
		this.customer = customer;
		this.expiration = expiration;
		this.seats = seats;
	}

	public SeatLease() {
		//empty
	}
	
	public BigDecimal getPrice() {
		return new BigDecimal(seating.getSeatPrice()).multiply(new BigDecimal(seats)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void expire() {
		seating.expire(this);
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public int getSeats() {
		return seats;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setSeating(EventSeating seating) {
		this.seating = seating;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public EventSeating getSeating() {
		return seating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatLease other = (SeatLease) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SeatLease [id=" + id + ", customer=" + customer + ", seats=" + seats + ", expiration=" + expiration
				+ ", seating=" + seating + "]";
	}

}
