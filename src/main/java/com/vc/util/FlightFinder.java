package com.vc.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Map;

public interface FlightFinder {
	Map<String, Object> getTripResults(String origin, String destination, Date departureDate, Date returnDate, int adult, int child, int infant) throws GeneralSecurityException, IOException;

}
