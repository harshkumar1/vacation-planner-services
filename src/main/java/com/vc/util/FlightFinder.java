package com.vc.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import com.vc.model.FlightSearchResponse;

public interface FlightFinder {
	FlightSearchResponse getTripResults(String origin, String destination, Date departureDate, Date returnDate, int adult, int child, int infant) throws GeneralSecurityException, IOException;

}
