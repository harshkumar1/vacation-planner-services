package com.vc.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.model.PassengerCounts;
import com.google.api.services.qpxExpress.model.SliceInput;
import com.google.api.services.qpxExpress.model.TripOption;
import com.google.api.services.qpxExpress.model.TripOptionsRequest;
import com.google.api.services.qpxExpress.model.TripsSearchRequest;
import com.google.api.services.qpxExpress.model.TripsSearchResponse;
import com.vc.model.FlightSearchResponse;

public class GoogleQpxExpress implements FlightFinder {

	private static final String APPLICATION_NAME = "vacation-planner";
	private static final String API_KEY = "AIzaSyBdVHqzy39BzWHoyPQmRQR2ovR32hZuEy0";

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public GoogleQpxExpress() {
	}

	@Override
	public FlightSearchResponse getTripResults(String origin, String destination, Date departureDate, Date returnDate, int adult, int child, int infant) throws GeneralSecurityException, IOException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		PassengerCounts passengers = new PassengerCounts();
		if (adult > 0) {
			passengers.setAdultCount(adult);
		}
		if (child > 0) {
			passengers.setChildCount(child);
		}
		if (infant > 0) {
			passengers.setInfantInSeatCount(infant);
		}

		List<SliceInput> slices = new ArrayList<SliceInput>();

		SliceInput departureSlice = getSliceInput(origin, destination, departureDate);
		slices.add(departureSlice);
		
		if (returnDate != null) {
			SliceInput returnSlice = getSliceInput(destination, origin, returnDate);
			slices.add(returnSlice);
		}
		
		TripOptionsRequest request = new TripOptionsRequest();
		request.setSolutions(50);
		request.setPassengers(passengers);
		request.setSlice(slices);

		TripsSearchRequest parameters = new TripsSearchRequest();
		parameters.setRequest(request);

		QPXExpress qpXExpress = new QPXExpress
				.Builder(httpTransport, JSON_FACTORY, null)
				.setApplicationName(APPLICATION_NAME)
				.setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(API_KEY))
				.build();

		TripsSearchResponse list = qpXExpress
				.trips()
				.search(parameters)
				.execute();

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonResponseTripOptions = new ObjectMapper().writeValueAsString(list.getTrips().getTripOption()).replace("////", "");
		String jsonResponseData = new ObjectMapper().writeValueAsString(list.getTrips().getData()).replace("////", "");
				//ow.writeValueAsString(list.getTrips().getData());
	
		FlightSearchResponse obj = new FlightSearchResponse(jsonResponseData, jsonResponseTripOptions);

		return  obj;
	}

	private SliceInput getSliceInput(String origin, String destination, Date travelDate) {
		SliceInput slice = new SliceInput();
		// NYC
		slice.setOrigin(origin);
		// LGA
		slice.setDestination(destination);
		// yyyy-MM-dd
		slice.setDate(df.format(travelDate));
		return slice;
	}

}
