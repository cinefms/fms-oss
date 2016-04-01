package com.openfms.core.api.v2.advice.editors;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.openfms.core.api.v2.utils.DateRange;
import com.openfms.model.core.project.FmsProjectHolder;

public class CustomDateRangeEditor extends PropertyEditorSupport {

	private static String[] names = new String[] { "w", "d", "h", "m", "s" }; 
	
	private static int[] fields = new int[] { Calendar.WEEK_OF_YEAR, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND }; 
	
	public static Date parse(TimeZone tz, String s) {
		Calendar c = Calendar.getInstance(tz);
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.MILLISECOND,0);

		for(int i=names.length-1;i>-1;i--) {
			if(s.endsWith(names[i])) {
				s = s.substring(0, s.length()-1);
				int a = Integer.parseInt(s); 
				c.set(fields[i], c.get(fields[i])+a);
				break;
			} else {
				c.set(fields[i], 0);
			}
		}
		return c.getTime();
	}
	
	
	public static DateRange getDateRange(String in) {
		if(in != null) {
			DateRange out = new DateRange();
			TimeZone tz = TimeZone.getDefault();
			try {
				tz = TimeZone.getTimeZone(FmsProjectHolder.get().getTimezone());
			} catch (Exception e) {
			}
			tz = TimeZone.getTimeZone("Europe/Berlin");
			List<Date> ds = new ArrayList<Date>(); 
			for(String s : in.split("\\|")) {
				ds.add(parse(tz,s));
			}
			if(ds.size()>0) {
				out.setFrom(ds.get(0));
			}
			if(ds.size()>1) {
				out.setTo(ds.get(1));
			}
			return out;
		}
		return null;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(getDateRange(text));
	}

	@Override
	public String getAsText() {
		return "";
	}
	
	
	
}