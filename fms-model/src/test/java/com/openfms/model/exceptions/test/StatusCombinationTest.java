package com.openfms.model.exceptions.test;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.openfms.model.utils.StatusCombine;

@RunWith(MockitoJUnitRunner.class)
public class StatusCombinationTest {
	
	@Test
	public void testA() {
		int a = StatusCombine.combine(3,2,2,0);
		Assert.assertEquals(0, a);
	}

	@Test
	public void testB() {
		int a = StatusCombine.combine(3,2,2,-1);
		Assert.assertEquals(2, a);
	}
	
	@Test
	public void testC() {
		int a = StatusCombine.combine(-1,3,2,2);
		Assert.assertEquals(2, a);
	}
	
	@Test
	public void testD() {
		int a = StatusCombine.combine(1,1,1);
		Assert.assertEquals(1, a);
	}
	
	@Test
	public void testE1() {
		int a = StatusCombine.combine(0,4,9);
		Assert.assertEquals(0, a);
	}
	
	@Test
	public void testE2() {
		int a = StatusCombine.combine(4,9);
		Assert.assertEquals(4, a);
	}
	

	@Test
	public void testF() {
		int a = StatusCombine.combine(-1,-1,-1);
		Assert.assertEquals(-1, a);
	}
	
	@Test
	public void testG() {
		int a = StatusCombine.combine();
		Assert.assertEquals(-1, a);
	}
	
	
}
