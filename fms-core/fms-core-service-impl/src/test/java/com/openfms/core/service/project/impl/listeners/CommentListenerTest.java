package com.openfms.core.service.project.impl.listeners;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommentListenerTest {

	@Test
	public void testGetPotentialUsernames() {
		
		CommentListener cl = new CommentListener();
		
		List<String> out = cl.getPotentialUsernames("hello @basti, this might be for you or @hasi88, @HASE or @h__00nd");
		Assert.assertNotNull(out);
		Assert.assertEquals(4, out.size());
		Assert.assertEquals("basti", out.get(0));
		Assert.assertEquals("hasi88", out.get(1));
		Assert.assertEquals("HASE", out.get(2));
		Assert.assertEquals("h__00nd", out.get(3));
	}
	
	
}
