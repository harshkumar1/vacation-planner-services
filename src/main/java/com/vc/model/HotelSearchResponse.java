package com.vc.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelSearchResponse {
	
	private String ch;
	private String gr;
	private String rtn;
	private String room_count;
	private String cn;
	private String prc;
	private String lid;
	private String tp;
	private String lo;
	private String la;
	private String tp_alltax;
	private ArrayList<String> tg;
	private String checkintime;
	private ArrayList<String> fm;
	private String c;
	private String checkouttime;
	private String reco_per;
	private String l;
	private String hn;
	private String hr;
	private Personalized_Keys personalized_keys;
	private String  tmob;
	private String tbig;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Personalized_Keys {
		private String np_wt;//": 5782,
		private String tp_alltax;//: 57820,
		private String tp;//: 49000
		
	}

	public String getTp_alltax() {
		return tp_alltax;
	}
	

}
