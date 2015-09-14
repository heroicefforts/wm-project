package com.acme.reservations.controller;

import java.util.List;

import com.acme.reservations.domain.Event;
import com.acme.reservations.web.json.CustomSerializer;
import com.acme.reservations.web.json.PagedResponse;
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

    protected String pagedResponse(String path, int page, int pageSize, List<Event> events) {
    	PagedResponse resp = new PagedResponse(page, pageSize, events.subList(0, Math.min(pageSize, events.size())));
    	if(page > 0)
    		resp.setPrevPage(path + "?page=" + (page - 1) + "&pageSize=" + pageSize);
    	if(events.size() > pageSize)
    		resp.setNextPage(path + "?page=" + (page + 1) + "&pageSize=" + pageSize);
    	
    	return ser.toJson(resp);
	}
    
    protected String invalidRequest(Request request) {
    	return ser.toJson(new Response(400, "Bad request.", request));
    }

}
