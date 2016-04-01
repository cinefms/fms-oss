package com.openfms.utils.common;

import java.security.SecureRandom;

public class GuidGenerator {

	private SecureRandom random;

	private static long start = 1269488707241l;

	// public static String goodChars =
	// "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_=";
	public static String goodChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzQQ";
	public static String pwChars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789abcdefghkmnopqrstuvwxyzQQ";
	private static int step = 0;
	static {
		String g = Integer.toBinaryString(goodChars.length() - 1);
		step = g.length();
		if (g.indexOf("0") > -1) {
			step = step - 1;
		}
	}
	private static int mask = (int) Math.pow(2, step) - 1;

	public GuidGenerator() {
		random = new SecureRandom();
	}

	private String getToken0() {
		return pack(random.nextLong(), 11, false);
	}

	public static String getSessionId() {
		return new GuidGenerator().getSessionId0();
	}

	private String getSessionId0() {
		return 
				pack(System.nanoTime(), 20, true) + 
				pack(random.nextLong(), 20, true) +
				pack(System.currentTimeMillis(), 20, true) + 
				pack(random.nextLong(), 20, true);

	}

	public static String getPassword(int length) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++) {
			int c=(int)Math.floor(Math.random()*pwChars.length());
			sb.append(pwChars.charAt(c));
		}
		return sb.toString();
	}
	

	public static String getToken() {
		return new GuidGenerator().getToken0();
	}
	
	private String getToken0(int length) {
		return pack(System.currentTimeMillis(), (int) Math.floor(length / 2d), false) + pack(random.nextLong(), (int) Math.ceil(length / 2d), false);

	}

	public static String getToken(int length) {
		return new GuidGenerator().getToken0(length);
	}

	public static String pack(long in, int maxLength, boolean skipZeros) {
		String s = "";
		long remainder = Math.abs(in);
		while (true) {
			int a = (int) (remainder & mask);
			s = goodChars.charAt(a) + s;
			remainder = remainder >> step;
			if (s.length() >= maxLength || (skipZeros && remainder == 0)) {
				break;
			}
		}
		return s;
	}

	private String getKey0() {
		StringBuilder sb = new StringBuilder();
		sb.append(pack(System.currentTimeMillis() - start, 11, true));
		sb.append('-');
		sb.append(pack(random.nextLong(), 11, false));
		sb.append('-');
		sb.append(pack(random.nextLong(), 11, false));
		return sb.toString();
	}

	public static String getKey() {
		return new GuidGenerator().getKey0();
	}
	
}