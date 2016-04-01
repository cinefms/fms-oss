package com.openfms.core.connector.base.impl;

public class UuidUtils {
	
	public static String toSimple(String in) {
		return in.replaceAll("(urn:uuid:)?(\\p{XDigit}{8})-(\\p{XDigit}{4})-(\\p{XDigit}{4})-(\\p{XDigit}{4})-(\\p{XDigit}{12})", "$2$3$4$5$6");
	}
	
	public static String toExtended(String in) {
		return in.replaceAll("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})", "urn:uuid:$1-$2-$3-$4-$5");
	}

}
