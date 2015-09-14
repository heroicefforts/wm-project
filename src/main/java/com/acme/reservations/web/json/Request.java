package com.acme.reservations.web.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interface implemented by complex web service requests.
 * 
 * @author jevans
 *
 */
public interface Request {
	/**
	 * @return true if the request is semantically valid.
	 */
	@JsonIgnore
	public boolean isValid();
}
