package com.acme.reservations.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A domain object representing the fulfillment of a client seat hold request including customer, seat leases, and external ids.
 * 
 * @author jevans
 *
 */
public class SeatHold implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Customer customer;
	private String guid;
	private String confirmation;
	private Date crteDttm;
	private int leaseCount;
	private List<SeatLease> leases;
	
	
	public SeatHold() {
		//empty
	}
	
	public SeatHold(Customer customer, List<SeatLease> leases) {
		this.customer = customer;
		this.leases = leases;
		this.leaseCount = leases.size();
		this.crteDttm = new Date();
		this.guid = UUID.randomUUID().toString();
	}

	public boolean isExpired() {
		return leaseCount > leases.size();
	}
	
	public Date getExpiry() {
		Date expiry = new Date(Long.MAX_VALUE);
		for(SeatLease lease : leases) {
			final Date lExpiry = lease.getExpiration();
			if(lExpiry.before(expiry))
				expiry = lExpiry;
		}
		
		return expiry;
	}

	public BigDecimal getPrice() {
		BigDecimal price = BigDecimal.ZERO;
		for(SeatLease lease : leases)
			price = price.add(lease.getPrice());
		
		return price;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public List<SeatLease> getLeases() {
		return leases;
	}

	public int getLeaseCount() {
		return leaseCount;
	}

	public void setLeaseCount(int leaseCount) {
		this.leaseCount = leaseCount;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String getConfirmation() {
		return confirmation;
	}
	
	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getCrteDttm() {
		return crteDttm;
	}

	public void setCrteDttm(Date crteDttm) {
		this.crteDttm = crteDttm;
	}
	
	public void setLeases(List<SeatLease> leases) {
		this.leases = leases;
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
		SeatHold other = (SeatHold) obj;
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
		return "SeatHold [id=" + id + ", guid=" + guid + ", customer=" + customer + ", crteDttm=" + crteDttm
				+ ", leases=" + leases + "]";
	}

	public Event getEvent() {
		return leases.get(0).getSeating().getEvent();
	}

}
