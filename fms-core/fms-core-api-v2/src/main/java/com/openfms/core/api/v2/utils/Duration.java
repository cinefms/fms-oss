package com.openfms.core.api.v2.utils;

public class Duration {
	
	private static char[] names = new char[] { 'w', 'd', 'h', 'm', 's' }; 
	
	private static long[] split = new long[] {
			7*24*60*60*1000,
			24*60*60*1000,
			60*60*1000,
			60*1000,
			1000
	};

	
	private String string;
	private long ms;

	public Duration() {
	}
	
	public Duration(String s) {
		this.ms = stringToMilliseconds(s);
		this.string = millisecondsToString(ms);
	}
	
	public Duration(long milliseconds) {
		this.ms = milliseconds;
		this.string = millisecondsToString(ms);
	}
	
	public long getMilliSeconds() {
		return ms;
	}

	public double getSeconds() {
		return (double)ms / (1000d);
	}
	
	public double getMinutes() {
		return (double)getSeconds() / (60d);
	}

	public double getHours() {
		return (double)getMinutes() / (60d);
	}
	
	public String getString() {
		return millisecondsToString(getMilliSeconds());
	}

	public String getDuration() {
		return string;
	}
	
	public static long stringToMilliseconds(String in) throws NumberFormatException {
		long out = 0l;
		try {
			for(String s : in.split(" ")) {
				long factor = 1;
				boolean cut = false;
				for(int i=0;i<names.length;i++) {
					if(s.endsWith(names[i]+"")) {
						factor = split[i];
						cut = true;
					}
				}
				if(cut) {
					s = s.substring(0, s.length()-1);
				}
				double d = Double.parseDouble(s);
				out = out + Math.round(d*factor); 
			}
		} catch (Exception e) {
			throw new NumberFormatException("parsing failed: "+e.getMessage());
		}
		return out;
	}
	

	public static String millisecondsToString(long in) throws NumberFormatException {
		String out = "";

		for(int i=0;i<names.length;i++) {
			long x = (long)Math.floor(in / split[i]);
			if(x>0) {
				out = out + " "+x+names[i];
				in = in % split[i];
			}
		}
		
		if(in > 0) {
			out = out + " "+in;
		}

		return out.trim();
	}
	
	
}
