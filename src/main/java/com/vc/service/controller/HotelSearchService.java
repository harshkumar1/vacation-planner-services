package com.vc.service.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.vc.model.Trip;
import com.vc.repository.HotelCity;

@RestController
public class HotelSearchService {

	private String APP_KEY = "1ce89f9b&app_key=72d4535f742f9ae9f9a6999745fef984";

	@Autowired
	private HotelCity hotelCityMap;

	@Produces(value="application/json")
	@RequestMapping(value="/hotels", method=RequestMethod.POST)
	public String getHotels(@RequestBody Trip trip) {
		String  resultJson = null;
		String cityId = "6771549831164675055"; //TBC
		String checkinDate = "20171009";//trip.getStartDate(); //Should be in format YYYYMMDD
		String checkoutDate = "20171019";//trip.getEndDate(); //Should be in format YYYYMMDD
		String adults = "1-1_0";//trip.getHotelInformation().getNumberOfGuests(); //"1-1_0" rooms-adults_children
		String pageNumber = "1";
		String url = "http://www.goibibo.com/hotels/search-data/?" + 
				"app_id=" + APP_KEY + 
				"&vcid=" + cityId +
				"&ci=" + checkinDate + "&co=" + checkoutDate + 
				"&r=" + adults + 
				"&pid=" + pageNumber;
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(url, String.class);

			System.out.println(result);
			
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(result);
			resultJson =  jsonObj.toJSONString();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		return resultJson;

	}

	public String getCityId(String cityName) {
		ArrayList<String> destCity = new ArrayList<>();
		destCity.add(cityName);
		List<com.vc.model.HotelCity> users = (List<com.vc.model.HotelCity>) hotelCityMap.findAll(destCity);
		return users.get(0).getCityId();
	}
}
