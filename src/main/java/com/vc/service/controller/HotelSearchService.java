package com.vc.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
		String checkinDate = getDate(trip.getStartDate()); //Should be in format YYYYMMDD
		String checkoutDate = getDate(trip.getEndDate()); //Should be in format YYYYMMDD
		String roomStringParam = trip.getHotelInformation().getNumberOfRooms() + "-" + trip.getHotelInformation().getNumberOfGuests() + "_0"; //"1-1_0" rooms-adults_children
		
		List<HotelSearchResponse> hotelResponses = queryMultiplePages(cityId, checkinDate, checkoutDate, roomStringParam);
		
		try {
			System.out.println("Response from Google API: " + new ObjectMapper().writeValueAsString(hotelResponses));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
	
		List<HotelSearchResponse> filteredHotelResponse = filterResponses(trip, hotelResponses);
		
		resultJson = new Gson().toJson(filteredHotelResponse);
		System.out.println("Response after filtering: " + resultJson);
		return resultJson;

	}

	/**
	 * API is querying GoIbibo 5 times to get the 5 pages worth results
	 * @param cityId
	 * @param checkinDate
	 * @param checkoutDate
	 * @param adults
	 * @return
	 */
	private List<HotelSearchResponse> queryMultiplePages(String cityId, String checkinDate, String checkoutDate, String adults) {
		List<Object> hotelResponsePaginated = null;//queryForHotels(cityId, checkinDate, checkoutDate, adults, "1");
		List<HotelSearchResponse> hotelResponseList = new ArrayList<HotelSearchResponse>();
		
		for (int pageNumber=1; pageNumber<=5; pageNumber++) {
			hotelResponsePaginated = queryForHotels(cityId, checkinDate, checkoutDate, adults, String.valueOf(pageNumber));
			if (hotelResponsePaginated == null) //Probably  a condition wherein results have been exhausted
				break;
			List<HotelSearchResponse> tempHotelResponseList = hotelResponsePaginated.stream().map(e -> {
				Map<String, Object> map1 = (LinkedTreeMap) e;
				return new ObjectMapper().convertValue(map1, HotelSearchResponse.class);
			}).collect(Collectors.toList());
			hotelResponseList.addAll(tempHotelResponseList);
		}
		
		return hotelResponseList;
	}

	/**
	 * Filtering based on following,
	 * 1. Star Rating
	 * 2. Price
	 * In-case of exception it catches and continues iterating
	 * @param trip
	 * @param hotelResponses
	 * @return
	 */
	private List<HotelSearchResponse> filterResponses(Trip trip, List<HotelSearchResponse> hotelResponses) {
		List<HotelSearchResponse> filteredHotelResponse = new ArrayList<HotelSearchResponse>();
		
		for (HotelSearchResponse hotelResponse : hotelResponses) {
			
			try {
				if ( Float.parseFloat(hotelResponse.getTp_alltax()) <= Float.parseFloat(trip.getBudgetLimit()) ) /* Price Filter */{
					Arrays.asList(trip.getHotelInformation().getRatings());
					double[] tripRatings = Arrays.stream(trip.getHotelInformation().getRatings()).mapToDouble(Double::parseDouble).toArray();
					double reponseRating = Double.parseDouble(hotelResponse.getGr());
					for (double tripRating : tripRatings) /* Rating Filter */{
						if (reponseRating >= tripRating)
							filteredHotelResponse.add(hotelResponse);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return filteredHotelResponse;
	}

	/**
	 * Querying for hotel based on pageNumber
	 * @param cityId
	 * @param checkinDate
	 * @param checkoutDate
	 * @param adults
	 * @param pageNumber
	 * @return
	 */
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hotelResponseList;
	}

	/**
	 * Getting cityId which is needed to be able to query for hotels.
	 * City Id list is provided by GoIbibo. The same is bundled as a resource here. Its queried for destination on which the search is happening
	 * @param cityName
	 * @return
	 */
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

	/**
	 * Called by getCityId() API. It just loads CSV to memory
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	private MappingIterator<CsvCityList> loadCityData() throws IOException, JsonProcessingException {
		CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
		CsvMapper mapper = new CsvMapper();
		ResourceLoader resourceLoader = new FileSystemResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:/city_list.csv");
		InputStream csvFileInputStream = resource.getInputStream();		
		MappingIterator<CsvCityList> readValues = mapper.reader(CsvCityList.class).with(bootstrapSchema).readValues(csvFileInputStream);

		return readValues;
	}

	
	private CsvCityList findCityIdByName(List<CsvCityList> cityList, String cityName) {
		return cityList.stream().filter(item -> item.getcity_name().equals(cityName)).findFirst().get();
	}

	/**
	 * Formatting date in the yyyyMMdd which is needed by the API
	 * @param dateInMillis
	 * @return
	 */
	private String getDate (String dateInMillis) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date departureDate = new Date (Long.parseLong(dateInMillis));
		String strDate = df.format(departureDate).toString();
		return strDate;
	}

}
