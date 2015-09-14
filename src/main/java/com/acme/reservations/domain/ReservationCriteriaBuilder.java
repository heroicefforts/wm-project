package com.acme.reservations.domain;

/**
 * Builder for more easily programatically generating a valid ReservationCriteria object.
 *  
 * @author jevans
 *
 */
public class ReservationCriteriaBuilder {
	private ReservationCriteria crit;
	
	public static ReservationCriteriaBuilder withSeats(int minSeats) {
		return new ReservationCriteriaBuilder(minSeats);
	}
	
	public ReservationCriteriaBuilder minLevel(int minLevel) {
		crit.setMinLevel(minLevel);
		return this;
	}
	
	public ReservationCriteriaBuilder maxLevel(int maxLevel) {
		crit.setMaxLevel(maxLevel);
		return this;
	}
	
	public ReservationCriteriaBuilder nonContiguous() {
		crit.setContiguous(false);
		return this;
	}
	
	public ReservationCriteria build() {
		return crit;
	}
	
	private ReservationCriteriaBuilder(int minSeats) {
		crit = new ReservationCriteria(minSeats);
	}

}
