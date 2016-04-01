package com.openfms.utils.common.reflection;

import java.util.Date;
import java.util.List;

public class RT1 {
	
	private List<RT1A> a;
	
	private String givenName;
	private String[] middleNames;
	private int integerNumber;
	private Long longNumber;
	private Double doubleDouble;
	private Date aDate;
	
	public List<RT1A> getRT1A() {
		return a;
	}


	public String getGivenName() {
		return givenName;
	}


	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}


	public String[] getMiddleNames() {
		return middleNames;
	}


	public void setMiddleNames(String[] middleNames) {
		this.middleNames = middleNames;
	}


	public int getIntegerNumber() {
		return integerNumber;
	}


	public void setIntegerNumber(int integerNumber) {
		this.integerNumber = integerNumber;
	}


	public Long getLongNumber() {
		return longNumber;
	}


	public void setLongNumber(Long longNumber) {
		this.longNumber = longNumber;
	}


	public Double getDoubleDouble() {
		return doubleDouble;
	}


	public void setDoubleDouble(Double doubleDouble) {
		this.doubleDouble = doubleDouble;
	}


	public Date getaDate() {
		return aDate;
	}


	public void setaDate(Date aDate) {
		this.aDate = aDate;
	}
	

}
