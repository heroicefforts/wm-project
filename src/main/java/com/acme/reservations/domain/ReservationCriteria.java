package com.acme.reservations.domain;

import java.io.Serializable;

/**
 * Contains the criteria required for a given seat hold request.
 * 
 * @author jevans
 *
 */
public class ReservationCriteria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int minSeats = -1;
	private Integer minLevel;
	private Integer maxLevel;
	private boolean contiguous = true;

	
	public ReservationCriteria(int seats) {
		this.minSeats = seats;
	}

	public ReservationCriteria() {
		//empty
	}
	
	public boolean matchesLevel(EventSeating seating) {
		int level = seating.getVenueSeating().getLevel(); 
		return (minLevel == null || minLevel <= level) && (maxLevel == null || maxLevel >= level);
	}
	
	/**
	 * @return the minimum venue level id from which seats may be drawn.
	 */
	public Integer getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(Integer minLevel) {
		if(maxLevel != null && maxLevel < minLevel)
			throw new IllegalArgumentException("Max level " + maxLevel + " must be greater than or equal to min level " + minLevel + ".");
		
		this.minLevel = minLevel;
	}

	/**
	 * @return the minimum number of seats required for a hold.
	 */
	public int getMinSeats() {
		return minSeats;
	}

	public void setMinSeats(int minSeats) {
		this.minSeats = minSeats;
	}

	/**
	 * @return the maximum venue level id from which seats may be drawn.
	 */
	public Integer getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(Integer maxLevel) {
		if(minLevel != null && minLevel > maxLevel)
			throw new IllegalArgumentException("Min level " + minLevel + " must be less than or equal to max level " + maxLevel + ".");
		
		this.maxLevel = maxLevel;
	}

	/**
	 * @return true if seats should all be within one level.
	 */
	public boolean isContiguous() {
		return contiguous;
	}

	public void setContiguous(boolean contiguous) {
		this.contiguous = contiguous;
	}

	/**
	 * @return true if the minimum required criteria has been supplied, i.e.:  minSeats.
	 */
	public boolean isValid() {
		return minSeats > -1;
	}
	
	@Override
	public String toString() {
		return "ReservationCriteria [minSeats=" + minSeats + ", minLevel=" + minLevel + ", maxLevel=" + maxLevel
				+ ", contiguous=" + contiguous + "]";
	}

}
