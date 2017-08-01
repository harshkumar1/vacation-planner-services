package com.vc.service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vc.model.HotelSearchResponse;
import com.vc.model.Trip;

@RestController
public class HotelSearchService {

	private String APP_ID = "1ce89f9b";
	private String APP_KEY = "72d4535f742f9ae9f9a6999745fef984";

	@Produces(value="application/json")
	@RequestMapping(value="/hotels", method=RequestMethod.POST)
	public String getHotels(@RequestBody Trip trip) throws JsonProcessingException {
		String  resultJson = null;
		String cityId = "6771549831164675055"; //TBC
		String checkinDate = "20171009";//trip.getStartDate(); //Should be in format YYYYMMDD
		String checkoutDate = "20171019";//trip.getEndDate(); //Should be in format YYYYMMDD
		String adults = "1-1_0";//trip.getHotelInformation().getNumberOfGuests(); //"1-1_0" rooms-adults_children
		String pageNumber = "1";
		String url = "http://www.goibibo.com/hotels/search-data/?" + 
				"app_id=" + APP_ID +
				"&app_key=" + APP_KEY +
				"&vcid=" + cityId +
				"&ci=" + checkinDate + "&co=" + checkoutDate + 
				"&r=" + adults + 
				"&pid=" + pageNumber;
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(url, String.class);
			Gson gson = new Gson();
			Map<String, Object> hotelObj = gson.fromJson(result, HashMap.class);
            List<Object> hotelResponseList = (ArrayList) hotelObj.entrySet().stream().findFirst().get().getValue();
            List<HotelSearchResponse> hotelResponses = hotelResponseList.stream().map(e -> {
                Map<String, Object> map1 = (LinkedTreeMap) e;
                return new ObjectMapper().convertValue(map1, HotelSearchResponse.class);
                //return hotelResponse;
            }).collect(Collectors.toList());
            
			System.out.println("The response is: " + new ObjectMapper().writeValueAsString(hotelResponses));
			System.out.println(result);
			
			resultJson = gson.toJson(hotelResponses);
			System.out.println(resultJson);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return resultJson;

	}

	/*public String getCityId(String cityName) {
		ArrayList<String> destCity = new ArrayList<>();
		destCity.add(cityName);
		List<com.vc.model.HotelCity> users = (List<com.vc.model.HotelCity>) hotelCityMap.findAll(destCity);
		return users.get(0).getCityId();
	}*/
}
