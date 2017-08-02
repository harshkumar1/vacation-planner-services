package com.vc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CsvCityList {

	private String city_name;
	
	public String getcity_name() {
		return city_name;
	}
	
	private String city_id;
	
	public String getcity_id() {
		return city_id;
		
	}
	
	private String domestic_flag;

}
