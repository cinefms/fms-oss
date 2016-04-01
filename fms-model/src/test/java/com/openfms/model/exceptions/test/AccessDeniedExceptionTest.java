package com.openfms.model.exceptions.test;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;

import com.openfms.model.exceptions.AccessDeniedException;

@RunWith(MockitoJUnitRunner.class)
public class AccessDeniedExceptionTest {
	
	@Test
	public void testInitializerExpectMessage() {
		AccessDeniedException ade = new AccessDeniedException();
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Assert.assertEquals("Access denied!", ade.getLocalizedMessage());
	}
	

}
