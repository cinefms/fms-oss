package com.openfms.tools.dcp;

import org.junit.Assert;
import org.junit.Test;

public class DCSubtitlesToSrtTest {

	
	@Test
	public void testFormatTime() {
		String x = DCSubtitlesToSrt.formatTime("00:00:00:112");
		Assert.assertEquals("00:00:00,112",x);
	}
	
	
}
