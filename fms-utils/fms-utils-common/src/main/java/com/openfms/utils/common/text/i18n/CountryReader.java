package com.openfms.utils.common.text.i18n;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CountryReader {
	
	private static HashMap<String, List<Country>> cByCode = new HashMap<String, List<Country>>();
	private static HashMap<String, Country> cByISO = new HashMap<String, Country>();
	private static List<Country> countries;
	
	private static void init() {
		if(countries!=null) {
			return;
		}
		BufferedReader br = null;
		InputStream is = null;
		countries = new ArrayList<Country>();
		try {
			is = CountryReader.class.getResourceAsStream("countries");
			br = new BufferedReader(new InputStreamReader(is));
			String s = null;
			while((s = br.readLine())!=null) {
				Country c = new Country();
				String[] x = s.split("\\t");
				c.setName(x[0].trim());
				c.addName("en",x[0].trim());
				if(x[1].indexOf(",")<0) {
					c.setCode(x[1].trim());
					for(int i=1;i<3;i++) {
						if(x[i].trim().length()>0) {
							c.addCode(x[i].trim());
						}
					}
				} else {
					c.addCode(x[3].trim());
					c.setCode(x[3].trim());
				}
				countries.add(c);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
			}
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
		
	}
	
	public static List<String> getIso639_3Codes() {
		init();
		return null;
	}
	
	public static Country getCountry(String iso3166) {
		init();
		Country out = cByISO.get(iso3166);
		if(out==null) {
			for(Country l : getCountries()) {
				if(l.getCode().compareToIgnoreCase(iso3166)==0) {
					out = l;
					break;
				}
			}
			cByISO.put(iso3166, out);
		}
		return out;
	}
	
	public static List<Country> getCountries(String code) {
		init();
		List<Country> out = cByCode.get(code);
		if(out==null) {
			out = new ArrayList<Country>();
			for(Country l : getCountries()) {
				if(l.getCodes().contains(code)) {
					out.add(l);
				}
			}
			cByCode.put(code, out);
		}
		return out;
	}

	public static List<Country> getCountries() {
		init();
		return Collections.unmodifiableList(countries);
	}

	public static void main(String[] args) {
		for(Country l : getCountries()) {
			System.err.println(l.getName()+"; "+l.getCodes());
		}
	}
	

	

}
