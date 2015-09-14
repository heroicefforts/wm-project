package com.acme.reservations.clock;

import java.time.Clock;

/**
 * Singleton for retrieving system time.  Aids in time based testing by allowing a simulation clock substitution.
 * 
 * @author jevans
 *
 */
public class Time {
	private static Clock clock = Clock.systemUTC();
	
	public static long getMillis() {
		return clock.millis();
	}
	
	static void setClock(Clock newClock) {
		clock = newClock;
	}
	
	private Time() {
		//empty
	}
}
