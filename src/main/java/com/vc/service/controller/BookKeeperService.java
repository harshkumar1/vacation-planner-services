package com.vc.service.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RestController
public class BookKeeperService {
	@Produces(value="application/json")
	@RequestMapping(value="/bookkeeper", method=RequestMethod.GET)
	public String getCurrency(@RequestParam("base") String base, @RequestParam("destination") String destination, @RequestParam("amount") String amount) {
		double conversionVal = -1;
		float amountConverted = 1;
		String url = "http://api.fixer.io/latest?base=" + base;
		URL obj;
		try {
			amountConverted = Float.parseFloat(amount);
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
				JSONParser parser = new JSONParser();
				JSONObject jsonObj = (JSONObject) parser.parse(br);
				JSONObject rates = (JSONObject) jsonObj.get("rates");
				Map<String, Object> rateResponseMap = new ObjectMapper().readValue(rates.toJSONString(), HashMap.class);
				conversionVal = (double) rateResponseMap.get(destination);
				//conversionVal = Float.parseFloat(conversionValue);
				con.disconnect();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		HashMap<String, Double> response = new HashMap<>();
		response.put("USD", conversionVal * amountConverted);
		
		Gson gsonObj = new Gson();
		String jsonResponse = gsonObj.toJson(response);
		
		return jsonResponse;
	}
}
