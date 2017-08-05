package com.vc.service.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.vc.model.Trip;
import com.vc.util.FlightFinder;
import com.vc.util.GoogleQpxExpress;

@RestController
public class FlightSearchService {

	@Produces(value="application/json")
	@RequestMapping(value="/flights", method=RequestMethod.POST)
	public String getFlight(@RequestBody Trip tripModel) {
		String jsonResponse = null;
		FlightFinder flightSearch = new GoogleQpxExpress();
		Date departureDate = new Date (Long.parseLong(tripModel.getStartDate()));
		Date returnDate = new Date (Long.parseLong(tripModel.getEndDate()));
		try {
			Map<String, Object> tripOptions = flightSearch.getTripResults(tripModel.getOrigin().getAirportCode(), tripModel.getDestination().getAirportCode(), departureDate, returnDate, Integer.parseInt(tripModel.getHotelInformation().getNumberOfGuests()), 0, 0);
			Gson gsonObj = new Gson();
			jsonResponse = gsonObj.toJson(tripOptions).replaceAll("\\\\","");
			
			System.out.println("Trip Options: " + jsonResponse);
		} catch (NumberFormatException | GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
		
		return jsonResponse;
	}

}
