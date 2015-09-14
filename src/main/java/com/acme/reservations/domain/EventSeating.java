package com.acme.reservations.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.reservations.clock.Time;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain object for event specific seating information.
 * 
 * @author jevans
 *
 */
public class EventSeating implements Comparable<EventSeating>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(EventSeating.class);
	
	private Integer id;
	
	@JsonBackReference
	private Event event;
	private VenueSeating venueSeating;
	private float seatPrice;
	
	@JsonIgnore
	private List<SeatLease> leases = new ArrayList<>();
	
	
	public EventSeating() {
		super();
	}
	
	public EventSeating(Integer id, Event event, VenueSeating venueSeating, float seatPrice, List<SeatLease> leases) {
		super();
		this.id = id;
		this.event = event;
		this.venueSeating = venueSeating;
		this.seatPrice = seatPrice;
		this.leases = leases;
	}

	public void setLeases(List<SeatLease> leases) {
		this.leases = leases;
		calculateAvailableSeats();
	}

	/**
	 * @return the remaining seats for this venue level:  total venue level seat count - current seat leases.
	 */
	public int getAvailableSeats() {
		return calculateAvailableSeats();
	}

	/**
	 * Attempts to hold the given number of seats at this level, purging expired leases if required.
	 * 
	 * @param customer the customer on whose behalf you are taking the lease.
	 * @param minSeats the number of seats required.
	 * @return a SeatLease, or null if there are insufficient seats availabel to meet the request.
	 */
	public SeatLease holdContiguousSeating(Customer customer, int minSeats) {
		SeatLease lease = null;
		if(getAvailableSeats() < minSeats)
			purgeExpiredLeases();
		
		if(getAvailableSeats() >= minSeats) {
			Date expiry = new Date(Time.getMillis() + event.getMaxHoldSecs() * 1000);
			lease = new SeatLease(this, customer, expiry, minSeats);
			addLease(lease);
		}
		
		return lease;
	}

	/**
	 * Attempts to hold up to the given number of seats at this level, purging expired leases if required.
	 * 
	 * @param customer the customer on whose behalf you are taking the lease.
	 * @param desiredSeats the maximum number of seats to hold.
	 * @return a SeatLease with seats between 1 and desiredSeats, null if 0 seats are available at this level.
	 */
	public SeatLease holdSeating(Customer customer, int desiredSeats) {
		SeatLease lease = null;
		
		if(getAvailableSeats() < desiredSeats)
			purgeExpiredLeases();

		if(getAvailableSeats() > 0) {
			int minSeats = Math.min(desiredSeats, getAvailableSeats());
			Date expiry = new Date(Time.getMillis() + event.getMaxHoldSecs() * 1000);
			lease = new SeatLease(this, customer, expiry, minSeats);
			addLease(lease);
		}
		
		return lease;
	}

	private int calculateAvailableSeats() {
		int remaining = venueSeating.getTotalSeats();
		for(SeatLease lease : leases)
			remaining -= lease.getSeats();
		
		return remaining;
	}

	private void purgeExpiredLeases() {
		logger.trace("Initiating purge of EventSeating:  {}.  Examining {} leases.", this, leases.size());
		Iterator<SeatLease> it = leases.iterator();
		while(it.hasNext()) {
			SeatLease lease = it.next();
			if(lease.getExpiration().getTime() < Time.getMillis()) {
				logger.trace("Purging expired lease {}", lease);
				it.remove();
			}
		}
	}

	private void addLease(SeatLease lease) {
		int newRemaining = getAvailableSeats() - lease.getSeats();
		if(newRemaining >= 0)
			leases.add(lease);
		else
			throw new IllegalArgumentException("Lease exceeds remaining seats by " + newRemaining + " seats.");
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public List<SeatLease> getLeases() {
		return leases;
	}

	public VenueSeating getVenueSeating() {
		return venueSeating;
	}
	
	public void setVenueSeating(VenueSeating venueSeating) {
		this.venueSeating = venueSeating;
	}
	
	public float getSeatPrice() {
		return seatPrice;
	}
	
	public void setSeatPrice(float seatPrice) {
		this.seatPrice = seatPrice;
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
		EventSeating other = (EventSeating) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(EventSeating other) {
		return this.venueSeating.compareTo(other.venueSeating);
	}

	@Override
	public String toString() {
		return "EventSeating [id=" + id + ", event=" + event + ", venueSeating=" + venueSeating + ", seatPrice="
				+ seatPrice + ", remainingSeats=" + getAvailableSeats() + "]";
	}

	public void expire(SeatLease seatLease) {
		leases.remove(seatLease);
	}

}
