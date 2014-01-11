package com.groupon.mobile.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class DateUtils {
	public static int calculateDayDifference(Date date) {
		LocalDate local1 = new LocalDate(date);
		LocalDate local2 = new LocalDate();

		return Days.daysBetween(local2, local1).getDays();
	}
	public static String ddMMYYYYtoMMddYYYY(String dateStr) {
		SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy");
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(date);		
	}
	public static String getCurrentDate(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}
}
