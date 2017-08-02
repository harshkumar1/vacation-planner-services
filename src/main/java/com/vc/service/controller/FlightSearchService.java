package com.vc.service.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.api.services.qpxExpress.model.TripOption;
import com.vc.model.Trip;
import com.vc.util.FlightFinder;
import com.vc.util.GoogleQpxExpress;

@RestController
public class FlightSearchService {

	@Produces(value = "application/json")
	@RequestMapping(value="/flights", method=RequestMethod.POST)
	public String getFlight(@RequestBody Trip tripModel) {
		String jsonResponse = null;
		FlightFinder flightSearch = new GoogleQpxExpress();
		Date departureDate = new Date (Long.parseLong(tripModel.getStartDate()));
		Date returnDate = new Date (Long.parseLong(tripModel.getEndDate()));
		try {
			List<TripOption> tripOptions = flightSearch.getTripResults(tripModel.getOrigin().getAirportCode(), tripModel.getDestination().getAirportCode(), departureDate, returnDate, Integer.parseInt(tripModel.getHotelInformation().getNumberOfGuests()), 0, 0);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			jsonResponse = ow.writeValueAsString(tripOptions);
			System.out.println("Trip Options: " + jsonResponse);
		} catch (NumberFormatException | GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		
		return jsonResponse;
	}

}
