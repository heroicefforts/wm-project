package com.acme.reservations.web.json;

import org.apache.commons.lang3.StringUtils;


public class ReserveRequest implements Request {
	private String customerEmail;
	private String holdId;
	
	
	public String getCustomerEmail() {
		return customerEmail;
	}
	
	public void setCustomerEmail(String customer) {
		this.customerEmail = customer;
	}
	
	public String getHoldId() {
		return holdId;
	}
	
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}

	@Override
	public boolean isValid() {
		return StringUtils.isNotBlank(customerEmail) && StringUtils.isNotBlank(holdId);
	}

}
