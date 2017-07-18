package com.vc.service.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.ws.rs.Produces;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vc.model.QPXExpressAirlineReservation;
import com.vc.model.Trip;

@RestController
public class FlightSearchService {

	private final String API_KEY="AIzaSyBdVHqzy39BzWHoyPQmRQR2ovR32hZuEy0";

	@Produces(value = "application/json")
	@RequestMapping(value="/flights", method=RequestMethod.POST)
	public String getFlight(@RequestBody Trip tripModel) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);

		QPXExpressAirlineReservation airlineReservation = new QPXExpressAirlineReservation();
		airlineReservation.setSolutions(10);

		airlineReservation.getRequest().getPassengers().setAdultCount(tripModel.getHotelPrefs().getNumberOfGuests());

		Date date = new Date(tripModel.getStartDate()); 

		airlineReservation.getSlice()[0].setDate(sdf.format(date));
		airlineReservation.getSlice()[0].setOrigin(tripModel.getOrigin().getAirportCode());
		airlineReservation.getSlice()[0].setDestination(tripModel.getDestination().getAirportCode());

		date = new Date(tripModel.getEndDate());
		airlineReservation.getSlice()[1].setDate(sdf.format(date));
		airlineReservation.getSlice()[1].setOrigin(tripModel.getDestination().getAirportCode());
		airlineReservation.getSlice()[1].setDestination(tripModel.getOrigin().getAirportCode());

		String url = "https://www.googleapis.com/qpxExpress/v1/trips/search?key=" + API_KEY;
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter strFlight = new StringWriter();

		URL obj = null;
		HttpURLConnection con = null;
		JSONArray  results = null;
		OutputStream os = null;
		
		try {
			objectMapper.writeValue(strFlight, airlineReservation);
			System.out.println(strFlight.toString());
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty( "charset", "utf-8");
			con.setRequestProperty( "Content-Length", Integer.toString( strFlight.toString().length() ));
			os = con.getOutputStream();
			os.write(strFlight.toString().getBytes());
			os.flush();
			os.close();
			
			int responseCode = con.getResponseCode();
			if(responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(con.getInputStream())));
				JSONParser parser = new JSONParser();
				JSONObject jsonObj = (JSONObject) parser.parse(br);
				results =  (JSONArray) jsonObj.get("results");
			}
		} catch ( IOException | ParseException e) {
			e.printStackTrace();
		}

		finally {
			
			con.disconnect();
		}
		
		System.out.println(results.toJSONString());
		return results.toJSONString();

	}

}
