package com.vc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Notification {
	private String alert;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private Date date;
	
	public Notification(String alert, Date date) {
		this.alert = alert;
		this.date = date;
	}
	
	public String getAlert() {
		return alert;
	}
	
	public Date getDate() {
		return date;
	}
}
