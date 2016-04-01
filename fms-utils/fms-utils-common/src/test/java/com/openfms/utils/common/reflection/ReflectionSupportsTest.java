package com.openfms.utils.common.reflection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ReflectionSupportsTest {

	@Test
	public void a() {
		YYY sss = new YYY();
		boolean t = ReflectionSupports.supports(sss.getClass(), String.class);
		Assert.assertEquals(true, t);
	}
	

}
