package com.acme.reservations.clock;

import java.time.Clock;
import java.time.Duration;

/**
 * Utility class for manipulating system time during tests.
 * 
 * @author jevans
 *
 */
public class TimeSimulator {
	/**
	 * Advance the current time by the given number of milliseconds.
	 * 
	 * @param millis
	 */
	public static void advanceTimeTo(long millis) {
		Clock clock = Clock.systemUTC();
		Duration offset = Duration.ofMillis(millis - clock.millis());
		Time.setClock(Clock.offset(clock, offset));
	}
}
