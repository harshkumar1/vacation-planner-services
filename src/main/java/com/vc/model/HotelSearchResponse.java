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
	public static class Personalized_Keys {
		private String np_wt;//": 5782,
		private String tp_alltax;//: 57820,
		private String tp;//: 49000
		public String getNp_wt() {
			return np_wt;
		}
		public void setNp_wt(String np_wt) {
			this.np_wt = np_wt;
		}
		public String getTp_alltax() {
			return tp_alltax;
		}
		public void setTp_alltax(String tp_alltax) {
			this.tp_alltax = tp_alltax;
		}
		public String getTp() {
			return tp;
		}
		public void setTp(String tp) {
			this.tp = tp;
		}
		
	}

	public String getTp_alltax() {
		return tp_alltax;
	}
	
	public void setTp_alltax(String tp_alltax) {
		this.tp_alltax = tp_alltax;
	}
	
	public String getTp() {
		return tp_alltax;
	}
	
	public String setTp(String tp) {
		return this.tp = tp;
	}

	public String getPrc() {
		return prc;
	}

	public void setPrc(String prc) {
		this.prc = prc;
	}

	public Personalized_Keys getPersonalized_keys() {
		return personalized_keys;
	}

	public String getGr() {
		return gr;
	}

	/*public void setPersonalized_keys(Personalized_Keys personalized_keys) {
		this.personalized_keys = personalized_keys;
	}*/
}
