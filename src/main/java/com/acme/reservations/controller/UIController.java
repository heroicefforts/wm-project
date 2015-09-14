package com.acme.reservations.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acme.reservations.dao.ReservationDao;
import com.acme.reservations.domain.Event;
import com.acme.reservations.web.json.View;

@Controller
@RequestMapping("/")
public class UIController extends AbstractJSONController {
	
	@Autowired
	private ReservationDao dao;
	
    @RequestMapping("/")
    public String home() {
    	return "home";
    }

    @RequestMapping(value="/event/{eventId}", method=RequestMethod.GET)
    public String eventPage(@PathVariable int eventId, Model model) {
    	model.addAttribute("event", eventId);
    	return "event";
    }

    @RequestMapping("/web/event")
    public @ResponseBody String eventPage(@RequestParam(value="page",defaultValue="0") int page, 
    		@RequestParam(value="pageSize", defaultValue="10") int pageSize, Model model) {
    	List<Event> events = dao.findEvents(page * pageSize, pageSize + 1);
    	return pagedResponse("/web/event", page, pageSize, events);
    }

	@RequestMapping(value="/web/event/{eventId}/venue", method=RequestMethod.GET)
    public @ResponseBody String hold(@PathVariable int eventId) {
    	Event event = dao.getEvent(eventId);
    	if(event != null)
    		return response(event, View.Detail.class);
    	else
    		return response(404, "No such event id:" + eventId);
    }
    
}
