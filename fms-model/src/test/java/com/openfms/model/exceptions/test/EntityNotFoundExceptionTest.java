package com.openfms.model.exceptions.test;

import java.util.Locale;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;

import com.openfms.model.exceptions.EntityNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class EntityNotFoundExceptionTest {
	
	@Test
	public void testInitializerExpectMessage() {
		EntityNotFoundException enfe = new EntityNotFoundException(String.class, "AAA");
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Assert.assertEquals("Unable to find java.lang.String with id AAA",enfe.getLocalizedMessage());
	}
	

}
