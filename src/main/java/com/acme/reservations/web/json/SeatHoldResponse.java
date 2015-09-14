package com.acme.reservations.web.json;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.acme.reservations.domain.SeatHold;
import com.acme.reservations.domain.SeatLease;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({ "holdId", "customerEmail" })
public class SeatHoldResponse implements Payload {
	@JsonIgnore
	private SeatHold hold;
	
	
	public SeatHoldResponse(SeatHold hold) {
		this.hold = hold;
	}
	
	public String getHoldId() {
		return hold.getGuid();
	}

	@JsonInclude(Include.NON_NULL)
	public String getConfirmationCode() {
		return hold.getConfirmation();
	}
	
	public String getCustomerEmail() {
		return hold.getCustomer().getEmail();
	}
	
	public BigDecimal getPrice() {
		return hold.getPrice();
	}
	
	public List<SeatLeaseResponse> getLevels() {
		List<SeatLeaseResponse> levels = new ArrayList<>();
		
		for(SeatLease lease : hold.getLeases())
			levels.add(new SeatLeaseResponse(lease));
		
		return levels;
	}
	
	
	@JsonPropertyOrder({ "level", "desc", "seats", "seatPrice" })
	public static class SeatLeaseResponse {
		@JsonIgnore
		private SeatLease lease;
		
		public SeatLeaseResponse(SeatLease lease) {
			this.lease = lease;
		}
		
		public int getLevel() {
			return lease.getSeating().getVenueSeating().getLevel();
		}
		
		public String getDesc() {
			return lease.getSeating().getVenueSeating().getName();
		}
		
		public int getSeats() {
			return lease.getSeats();
		}

		public BigDecimal getSeatPrice() {
			return new BigDecimal(lease.getSeating().getSeatPrice()).setScale(2, RoundingMode.HALF_EVEN);
		}
		
	}
	
}
