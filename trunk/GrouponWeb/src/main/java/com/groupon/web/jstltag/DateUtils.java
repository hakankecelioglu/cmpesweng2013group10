package com.groupon.web.jstltag;

import java.util.Collection;
import java.util.Date;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class DateUtils {
	public static int calculateDayDifference(Date date) {
		LocalDate local1 = new LocalDate(date);
		LocalDate local2 = new LocalDate();

		return Days.daysBetween(local2, local1).getDays();
	}

	public static boolean isCollection(Object object) {
		return object instanceof Collection<?>;
	}
}
