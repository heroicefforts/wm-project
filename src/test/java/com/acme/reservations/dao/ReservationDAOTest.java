package com.acme.reservations.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.acme.reservations.domain.Customer;
import com.acme.reservations.domain.Event;
import com.acme.reservations.domain.EventSeating;
import com.acme.reservations.domain.Venue;
import com.acme.reservations.domain.VenueSeating;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/root-context.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ReservationDAOTest {
	
	@Autowired
	private ReservationDAO dao;
	
	@Autowired
	private DataSource ds;

	@Test
	public void testFindCustomerByEmail() {
		Customer jess = dao.findCustomerByEmail("jevans@test.com");
		assertNotNull(jess);
		assertEquals("Jess", jess.getFirstName());
		assertEquals("Evans", jess.getLastName());
	}
	
	@Test
	public void testFindCustomerByEmailReturnsNullOnMiss() {
		assertNull(dao.findCustomerByEmail("idonotexist@foo.org"));
	}
	
	@Test
	public void testEventRetrieval() {
		Event kamelot = dao.getEvent(1);
		assertNotNull("Could not locate event with id:1", kamelot);
		assertEquals("Expected event id:1 to be Kamelot", "Kamelot & DragonForce", kamelot.getName());

		Venue hob = kamelot.getVenue();
		assertNotNull(hob);
		assertEquals("Vegas House of Blues", hob.getName());
		assertNotNull(hob.getSeating());
		assertEquals(4, hob.getSeating().size());
		int i = 1;
		for(VenueSeating seat : hob.getSeating()) {
			assertEquals(i++, seat.getLevel());
		}
		
		assertNotNull(kamelot.getSeating());
		assertEquals(4, kamelot.getSeating().size());
		i = 1;
		for(EventSeating seat : kamelot.getSeating()) {
			assertNotNull(seat.getVenueSeating());
			assertEquals(i++, seat.getVenueSeating().getLevel());
		}
	}
	
}
