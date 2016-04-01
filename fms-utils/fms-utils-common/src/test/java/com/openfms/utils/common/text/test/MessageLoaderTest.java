package com.openfms.utils.common.text.test;

import java.util.Locale;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.openfms.utils.common.text.MessageLoader;
import com.openfms.utils.common.text.test.t1.A;
import com.openfms.utils.common.text.test.t2.B;
import com.openfms.utils.common.text.test.t3.xxx.C;
import com.openfms.utils.common.text.test.t4.D;
import com.openfms.utils.common.text.test.t5.E;

@RunWith(MockitoJUnitRunner.class)
public class MessageLoaderTest {
	
	
	@Test
	public void testClassSpecificExpectClassSpecificMessage() {
		MessageSource messageSource = MessageLoader.getMessageSource(A.class);
		Assert.assertEquals("Test Message For A: A", messageSource.getMessage("TEST", new Object[] { "A" }, Locale.ENGLISH));
	}
	
	@Test
	public void testPackageSpecificExpectPackageSpecificMessage() {
		MessageSource messageSource = MessageLoader.getMessageSource(B.class);
		Assert.assertEquals("Test Message For B: B", messageSource.getMessage("TEST", new Object[] { "B" }, Locale.ENGLISH));
	}
	
	@Test
	public void testPackageParentExpectPackageParentMessage() {
		MessageSource messageSource = MessageLoader.getMessageSource(C.class);
		Assert.assertEquals("Test Message For C: C", messageSource.getMessage("TEST", new Object[] { "C" }, Locale.ENGLISH));
	}
	
	@Test(expected=NoSuchMessageException.class)
	public void testNoMessageExpectException() {
		MessageSource messageSource = MessageLoader.getMessageSource(D.class);
		messageSource.getMessage("TEST", new Object[] { "B" }, Locale.ENGLISH);
	}

	@Test
	public void testPackageWithNameExpectPackageWithNameMessage() {
		MessageSource messageSource = MessageLoader.getMessageSource(E.class, "test");
		Assert.assertEquals("Test Message For E: E", messageSource.getMessage("TEST", new Object[] { "E" }, Locale.ENGLISH));
	}
	
	
}
