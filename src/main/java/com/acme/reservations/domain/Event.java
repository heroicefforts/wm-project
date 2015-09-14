package com.acme.reservations.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.reservations.exception.SeatHoldExpiredException;
import com.acme.reservations.web.json.Payload;
import com.acme.reservations.web.json.View;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Domain object handling state for a given event.
 * 
 * @author jevans
 *
 */
public class Event implements Serializable, Payload {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Event.class);

	private Integer id;
	private String name;
	private Venue venue;
	private Date startDttm;
	private Date endDttm;
	private int maxHoldSecs;
	
	@JsonView(View.Detail.class)
	private SortedSet<EventSeating> seating;
	
	
	public Event() {
		super();
	}

	public Event(Integer id, Venue venue, String name, SortedSet<EventSeating> seating) {
		super();
		this.id = id;
		this.venue = venue;
		this.name = name;
		this.seating = seating;
	}

	/**
	 * Returns the unleased seats for a given venue and level.
	 * 
	 * @param venueLevel the particular level, null for all levels.
	 * @return the unheld seat count
	 */
	public int getAvailableSeatsForVenue(Integer venueLevel) {
		int available = 0;
		for(EventSeating es : seating) {
			if(venueLevel == null || venueLevel == es.getVenueSeating().getLevel())
				available += es.getAvailableSeats();
		}
		
		return available;
	}
	
	/**
	 * Attempts to locate and hold available seating matching the reservation criteria.
	 * 
	 * @param customer the customer on whose behalf you are reserving the seats.
	 * @param crit the reservation criteria.
	 * @return a seat hold, null if the match could not be fulfilled.
	 */
	public SeatHold holdSeating(Customer customer, ReservationCriteria crit) {
		SortedSet<EventSeating> seating = new TreeSet<>(Collections.reverseOrder());
		seating.addAll(this.seating);

		List<SeatLease> leases = new ArrayList<>();
		if(crit.isContiguous()) {
			for(EventSeating es : seating) {
				if(crit.matchesLevel(es)) {
					SeatLease lease = es.holdContiguousSeating(customer, crit.getMinSeats());
					if(lease != null) {
						System.out.println(customer);
						logger.debug("Customer<{}>:  Attained lease {}.", customer.getEmail(), lease);
						leases.add(lease);
						break;
					}
				}
			}
		}
		else {
			int remainingSeats = crit.getMinSeats();
			for(EventSeating es : seating) {
				if(crit.matchesLevel(es)) {
					SeatLease lease = es.holdSeating(customer, remainingSeats);
					if(lease != null) {
						leases.add(lease);
						remainingSeats -= lease.getSeats();
					}
				}
			}
			
			if(remainingSeats > 0) {
				for(SeatLease lease : leases)
					lease.expire();
				leases.clear();
			}
		}
		
		SeatHold hold = null;
		if(!leases.isEmpty())
			hold = new SeatHold(customer, leases);
		else
			logger.debug("Customer<{}>:  Could not attain lease(s) for criteria:  {}.", customer.getEmail(), crit);
		
		return hold;
	}

	/**
	 * Reserve the seat hold.
	 * @param hold
	 */
	public void reserve(SeatHold hold) {
		if(!hold.isExpired()) {
			for(SeatLease lease : hold.getLeases())
				lease.setExpiration(endDttm);
	
			hold.setConfirmation(UUID.randomUUID().toString());
		}
		else
			throw new SeatHoldExpiredException(hold);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Venue getVenue() {
		return venue;
	}
	
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public SortedSet<EventSeating> getSeating() {
		return seating;
	}
	
	public void setSeating(SortedSet<EventSeating> seating) {
		this.seating = seating;
	}

	public Date getStartDttm() {
		return startDttm;
	}
	
	public void setStartDttm(Date startDttm) {
		this.startDttm = startDttm;
	}

	public Date getEndDttm() {
		return endDttm;
	}

	public void setEndDttm(Date endDttm) {
		this.endDttm = endDttm;
	}

	
	public int getMaxHoldSecs() {
		return maxHoldSecs;
	}

	
	public void setMaxHoldSecs(int maxHoldSecs) {
		this.maxHoldSecs = maxHoldSecs;
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
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
