package com.acme.reservations.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Domain object representing the physical characteristics of one seating level within a venue.
 * 
 * @author jevans
 *
 */
public class VenueSeating implements Comparable<VenueSeating>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private int level;
	private int rows;
	private int seatsPerRow;
	
	@JsonBackReference
	private Venue venue;
	
	
	public VenueSeating() {
		super();
	}
	
	public VenueSeating(Integer id, Venue venue, int level, String name, int rows, int seatsPerRow) {
		super();
		this.id = id;
		this.venue = venue;
		this.level = level;
		this.name = name;
		this.rows = rows;
		this.seatsPerRow = seatsPerRow;
	}

	public int getTotalSeats() {
		return rows * seatsPerRow;
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
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public int getSeatsPerRow() {
		return seatsPerRow;
	}
	
	public void setSeatsPerRow(int seatsPerRow) {
		this.seatsPerRow = seatsPerRow;
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
		VenueSeating other = (VenueSeating) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(VenueSeating other) {
		if(other == null || this.level > other.level)
			return 1;
		else if(this.level < other.level)
			return -1;
		else
			return 0;
	}
	
	@Override
	public String toString() {
		return "VenueSeating [id=" + id + ", venue=" + venue + ", level=" + level + ", name=" + name + ", rows=" + rows
				+ ", seatsPerRow=" + seatsPerRow + "]";
	}

}
