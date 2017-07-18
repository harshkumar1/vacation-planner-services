package com.vc.model;

public class Trip {
	
	private int budgetLimit;
	private String currency;
	private long startDate;
	private long endDate;
	private Hotel hotelInformation;
	private City origin;
	private City destination;
	
	public int getBudgetLimit() {
		return budgetLimit;
	}

	public String getCurrency() {
		return currency;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public Hotel getHotelPrefs() {
		return hotelInformation;
	}

	public City getOrigin() {
		return origin;
	}

	public City getDestination() {
		return destination;
	}
	
	public class Hotel {
		
		private int numberOfGuests;
		private int numberOfRooms;
		private String[] hotelRating;

		public int getNumberOfGuests() {
			return numberOfGuests;
		}

		public int getNumberOfRooms() {
			return numberOfRooms;
		}

		public String[] getHotelRating() {
			return hotelRating;
		}

	}
	
	public class City {
		private String airportCode;
		private String city;
		private String country;

		public String getAirportCode() {
			return airportCode;
		}

		public String getCity() {
			return city;
		}

		public String getCountry() {
			return country;
		}
	}
	
	
}
