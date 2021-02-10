package org.zaleuco.demo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] a) {

		Calendar c = new GregorianCalendar(1995, 11, 23);
		String s = String.format("Duke's Birthday: %1$tm %1$te,%1$tY", c);
		System.out.println(s);

		Date d = new Date();
		System.out.println(String.format("Duke's Birthday: %1$tm %1$te,%1$tY", d));
		System.out.println(String.format("%1$td/%1$tm/%1$ty", d));
	}
}
