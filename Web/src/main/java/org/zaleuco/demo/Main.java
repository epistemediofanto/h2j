package org.zaleuco.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] a) {

//		Calendar c = new GregorianCalendar(1995, 11, 23);
//		String s = String.format("Duke's Birthday: %1$tm %1$te,%1$tY", c);
//		System.out.println(s);

		// iso 8601 yyyy-MM-dd 'T' HH:mm:ss. SSSZ.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Date d = new Date();
		System.out.println(sdf.format(d));

		System.out.println(String.format("%1$tY-%1$tm-%1$teT%1$tH:%1$tM:%1$tS.000%1$tz", d));
		System.out.println(String.format("%1$td/%1$tm/%1$ty", d));

		System.out.println(String.format("%3.3f", 123.12));
	}
}
