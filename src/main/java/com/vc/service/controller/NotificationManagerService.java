package com.vc.service.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vc.model.Notification;

@RestController
public class NotificationManagerService {
	
	//@CrossOrigin(origins = "http://localhost:8100")
	@RequestMapping("/notification")
    public ArrayList<Notification> index() {
        return getNotifications();
	}

	private ArrayList<Notification> getNotifications() {
		ArrayList<Notification> notifications = new ArrayList<>();
		notifications.add(new Notification("Fly with Emirates", getDate("2017-06-18")));
		notifications.add(new Notification("Fly with Etihad", getDate("2017-06-17")));
		
		return notifications;
	}
	
	private Date getDate(String sDate) {
		Date date = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("IST"));
		try {
			date = df.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}