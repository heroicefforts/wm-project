package com.acme.reservations.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.acme.reservations.clock.TimeSimulator;
import com.acme.reservations.domain.ReservationCriteria;
import com.acme.reservations.domain.ReservationCriteriaBuilder;
import com.acme.reservations.domain.SeatHold;
import com.acme.reservations.domain.SeatLease;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/root-context.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TicketServiceImplTest {
	private static final int KAMELOT = 1;
	private static final String JEVANS_EMAIL = "jevans@test.com";

	@Autowired
	private TransactionTemplate trans;

	@Autowired
	private SessionFactory factory;
	
	@Autowired
	private TicketService svc;

	@Test
	public void testTotalVenueSeatCountWhenEmpty() {
		assertEquals(25 * 50 + 20 * 100 + 15 * 100 + 15 * 100, svc.numSeatsAvailable(KAMELOT, null));
	}
	
	@Test
	public void testLevelVenueSeatCountWhenEmpty() {
		assertEquals(25 * 50, svc.numSeatsAvailable(KAMELOT, 1));
		assertEquals(15 * 100, svc.numSeatsAvailable(KAMELOT, 4));
	}

	@Test
	public void testLevelVenueSeatCountGracefullyHandlesInvalidLevel() {
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, 5));
	}

	@Test
	public void testHoldSeatTrivialCase() {
		int origSeats = svc.numSeatsAvailable(KAMELOT, 1);
		ReservationCriteria crit = ReservationCriteriaBuilder.withSeats(2).minLevel(1).maxLevel(1).build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, crit);
		assertEquals(origSeats - 2, svc.numSeatsAvailable(KAMELOT, 1));
		assertNotNull(hold);
		for(SeatLease lease : hold.getLeases())
			assertEquals(1, lease.getSeating().getVenueSeating().getLevel());
	}
	
	@Test
	public void testHoldSeatOneLevelToExhaustion() {
		int origSeats = svc.numSeatsAvailable(KAMELOT, 1);
		System.out.println(origSeats + " seats available.");
		assertTrue("Expected event orchestra seating.", origSeats % 2 == 0);
		ReservationCriteria crit = ReservationCriteriaBuilder.withSeats(origSeats / 2).minLevel(1).maxLevel(1).build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, crit);
		assertNotNull(hold);
		assertEquals(origSeats - origSeats / 2, svc.numSeatsAvailable(KAMELOT, 1));
		SeatHold hold2 = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, crit);
		assertNotNull(hold2);
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, 1));
		assertNull(svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, crit));
	}

	@Test
	public void testHoldSeatAcceptsLessDesirableLevel() {
		int levelOneSeats = svc.numSeatsAvailable(KAMELOT, 1);
		ReservationCriteria fill = ReservationCriteriaBuilder.withSeats(levelOneSeats).minLevel(1).maxLevel(1).build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, fill);
		assertNotNull(hold);

		int levelTwoSeats = svc.numSeatsAvailable(KAMELOT, 2);
		ReservationCriteria crit = ReservationCriteriaBuilder.withSeats(2).minLevel(1).maxLevel(3).build();
		SeatHold hold2 = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, crit);
		assertEquals(levelTwoSeats - 2, svc.numSeatsAvailable(KAMELOT, 2));
		assertNotNull(hold2);
		assertEquals(2, hold2.getLeases().get(0).getSeating().getVenueSeating().getLevel());
	}

	@Test
	public void testBuyOut() {
		int totalSeats = svc.numSeatsAvailable(KAMELOT, null);
		ReservationCriteria consume = ReservationCriteriaBuilder.withSeats(totalSeats).nonContiguous().build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, consume);
		assertNotNull(hold);
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, null));
		SeatHold hold2 = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, consume);
		assertNull(hold2);
	}

	@Test
	public void testHoldSeatAndReserveTrivialCase() {
		ReservationCriteria crit = ReservationCriteriaBuilder.withSeats(2).minLevel(1).maxLevel(1).build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, crit);
		assertNotNull(hold);
		System.out.println(svc.reserveSeats(JEVANS_EMAIL, hold.getGuid()));
	}

	@Test
	public void testHoldsAreExpiredOverTime() {
		int totalSeats = svc.numSeatsAvailable(KAMELOT, 1);
		ReservationCriteria consume = ReservationCriteriaBuilder.withSeats(totalSeats).maxLevel(1).build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, consume);
		assertNotNull(hold);
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, 1));
		
		TimeSimulator.advanceTimeTo(hold.getExpiry().getTime() + 1);
		
		SeatHold hold2 = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, consume);
		assertNotNull(hold2);
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, 1));
	}
	
	@Test
	public void testNonContigHoldsAreExpiredOverTime() {
		int totalSeats = svc.numSeatsAvailable(KAMELOT, null);
		ReservationCriteria consume = ReservationCriteriaBuilder.withSeats(totalSeats).nonContiguous().build();
		SeatHold hold = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, consume);
		assertNotNull(hold);
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, null));
		
		TimeSimulator.advanceTimeTo(hold.getExpiry().getTime() + 1);
		
		SeatHold hold2 = svc.findAndHoldSeats(KAMELOT, JEVANS_EMAIL, consume);
		assertNotNull(hold2);
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, null));
	}

	@Test
	@DirtiesContext
	public void testConcurrentHolds() {
		int totalSeats = svc.numSeatsAvailable(KAMELOT, 1);
		ReservationCriteria crit = ReservationCriteriaBuilder.withSeats(2).maxLevel(1).build();
		assertEquals(0, totalSeats % 2);
		List<Hold> holds = new ArrayList<>();
		ExecutorService exec = Executors.newFixedThreadPool(4);
		for(int i = 0; i < totalSeats / 2; i++) {
			Hold hold = new Hold(svc, KAMELOT, JEVANS_EMAIL, crit, true);
			holds.add(hold);
			exec.submit(hold);
		}
		
		exec.shutdown();
		try {
			if(!exec.awaitTermination(10, TimeUnit.SECONDS))
				fail("Concurrent execution exceed 10 seconds.");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		for(Hold hold : holds) {
			if(!hold.succeeded())
				fail("Hold failed:  " + hold);
		}

		factory.getCurrentSession().clear();
		assertEquals(0, svc.numSeatsAvailable(KAMELOT, 1));
	}
	
    private class Hold implements Runnable {
		private TicketService svc;
		private int eventId;
		private String customerEmail;
		private ReservationCriteria crit;
		private boolean success;
		private SeatHold hold;

		public Hold(TicketService svc, int eventId, String customerEmail, ReservationCriteria crit, boolean success) {
			super();
			this.svc = svc;
			this.eventId = eventId;
			this.customerEmail = customerEmail;
			this.crit = crit;
			this.success = success;
		}

		@Override
		public void run() {
			trans.execute(new TransactionCallbackWithoutResult() {
				@Override
				public void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						hold = svc.findAndHoldSeats(eventId, customerEmail, crit);
					}
					catch(Exception e) {
						e.printStackTrace();
						status.setRollbackOnly();
					}
				}
			});
		}
		
		public boolean succeeded() {
			if(success)
				return hold != null;
			else
				return hold == null;
		}
	}
	
}
