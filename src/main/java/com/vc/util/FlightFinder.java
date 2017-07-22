package com.vc.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import com.google.api.services.qpxExpress.model.TripOption;

public interface FlightFinder {

	List<TripOption> getTripResults(String origin, String destination, Date departureDate, Date returnDate, int adult, int child, int infant) throws GeneralSecurityException, IOException;

}
