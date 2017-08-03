package com.vc.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Produces;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vc.model.CsvCityList;
import com.vc.model.HotelSearchResponse;
import com.vc.model.Trip;

@RestController
public class HotelSearchService {

	private String APP_ID = "1ce89f9b";
	private String APP_KEY = "72d4535f742f9ae9f9a6999745fef984";
	private static List<CsvCityList> cityList = null;
	
	@Produces(value="application/json")
	@RequestMapping(value="/hotels", method=RequestMethod.POST)
	public String getHotels(@RequestBody Trip trip) throws ParseException {
		
		String  resultJson = null;
		String cityId = getCityId(trip.getDestination().getCity());
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date departureDate = new Date (Long.parseLong(trip.getStartDate()));
		Date returnDate = new Date (Long.parseLong(trip.getEndDate()));
		String checkinDate = df.format(departureDate).toString(); //Should be in format YYYYMMDD
		String checkoutDate = df.format(returnDate).toString(); //Should be in format YYYYMMDD
		String adults = trip.getHotelInformation().getNumberOfRooms() + "-" + trip.getHotelInformation().getNumberOfGuests() + "_0";//trip.getHotelInformation().getNumberOfGuests(); //"1-1_0" rooms-adults_children
		String pageNumber = "1";
		
		queryForHotels(cityId, checkinDate, checkoutDate, adults, pageNumber);
		
		List<Object> hotelResponseList = queryForHotels(cityId, checkinDate, checkoutDate, adults, pageNumber);
		List<HotelSearchResponse> hotelResponses = hotelResponseList.stream().map(e -> {
			Map<String, Object> map1 = (LinkedTreeMap) e;
			return new ObjectMapper().convertValue(map1, HotelSearchResponse.class);
			//return hotelResponse;
		}).collect(Collectors.toList());
		
		hotelResponseList = queryForHotels(cityId, checkinDate, checkoutDate, adults, pageNumber+1);
		List<HotelSearchResponse> hotelResponses1 = hotelResponseList.stream().map(e -> {
			Map<String, Object> map1 = (LinkedTreeMap) e;
			return new ObjectMapper().convertValue(map1, HotelSearchResponse.class);
			//return hotelResponse;
		}).collect(Collectors.toList());
		
		hotelResponses.addAll(hotelResponses1);
		try {
			System.out.println("The response is: " + new ObjectMapper().writeValueAsString(hotelResponses));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		List<HotelSearchResponse> filteredHotelResponse = new ArrayList<HotelSearchResponse>();
		
		for (HotelSearchResponse hotelResponse : hotelResponses) {
			if ( Float.parseFloat(hotelResponse.getTp_alltax()) <= Float.parseFloat(trip.getBudgetLimit()) ) {
				filteredHotelResponse.add(hotelResponse);
			}
		}
		
		resultJson = new Gson().toJson(filteredHotelResponse);
		System.out.println(resultJson);
		return resultJson;

	}

	private List<Object> queryForHotels(String cityId, String checkinDate, String checkoutDate, String adults,
			String pageNumber) {
		List<HotelSearchResponse> hotelResponses = null;
		List<Object> hotelResponseList = null;
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
			Map<String, Object> hotelObj = new Gson().fromJson(result, HashMap.class);
			hotelResponseList = (ArrayList) hotelObj.entrySet().stream().findFirst().get().getValue();
			//LinkedTreeMap hotelResponseList = (LinkedTreeMap) hotelObj.entrySet().stream().findFirst().get().getValue();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hotelResponseList;
	}

	public String getCityId(String cityName) {
		if (cityList == null) {
			try {
				MappingIterator<CsvCityList> readValues = loadCityData();
				cityList = readValues.readAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CsvCityList city = findCityIdByName(cityList, cityName);
		System.out.println("city name : " + city.getcity_name());
		System.out.println("city id : " + city.getcity_id());
		return city.getcity_id();
	}

	private MappingIterator<CsvCityList> loadCityData() throws IOException, JsonProcessingException {
		CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
		CsvMapper mapper = new CsvMapper();
		ResourceLoader resourceLoader = new FileSystemResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:/city_list.csv");
		
		InputStream csvFileInputStream = resource.getInputStream();
		
		MappingIterator<CsvCityList> readValues = mapper.reader(CsvCityList.class).with(bootstrapSchema).readValues(csvFileInputStream);
		/*MappingIterator<CsvCityList> readValues = 
				mapper.reader(CsvCityList.class).with(bootstrapSchema).readValues(file);*/
		return readValues;
	}

	private CsvCityList findCityIdByName(List<CsvCityList> cityList, String cityName) {
		return cityList.stream().filter(item -> item.getcity_name().equals(cityName)).findFirst().get();
	}


}
