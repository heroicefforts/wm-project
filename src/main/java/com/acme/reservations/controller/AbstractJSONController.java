package com.acme.reservations.controller;

import com.acme.reservations.web.json.CustomSerializer;
import com.acme.reservations.web.json.Payload;
import com.acme.reservations.web.json.Request;
import com.acme.reservations.web.json.Response;
import com.acme.reservations.web.json.View;

/**
 * Common class for handling common JSON service behavior.
 * 
 * @author jevans
 *
 */
public abstract class AbstractJSONController {

	private CustomSerializer ser;

	
	public AbstractJSONController() {
		ser = new CustomSerializer();
	}
	
    protected String response(Payload payload) {
    	return ser.toJson(new Response(payload));
    }
    
    protected String response(Payload payload, Class<? extends View> view) {
    	return ser.toJson(new Response(payload), view);
    }
    
    protected String response(int status, String errorMsg) {
    	return ser.toJson(new Response(status, errorMsg));
    }

    protected String invalidRequest(Request request) {
    	return ser.toJson(new Response(400, "Bad request.", request));
    }

}
