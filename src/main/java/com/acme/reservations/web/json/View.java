package com.acme.reservations.web.json;

/**
 * Marker interface for tailoring Jackson marshalling.
 * 
 * @author jevans
 *
 */
public class View {
	public static class Summary extends View {}
	public static class Detail extends Summary {}
}
