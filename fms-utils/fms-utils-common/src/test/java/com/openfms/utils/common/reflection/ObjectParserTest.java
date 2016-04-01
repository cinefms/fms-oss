package com.openfms.utils.common.reflection;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class ObjectParserTest {
	
	
	private ObjectParser objectParser;
	
	
	@Test
	public void testSchema1() throws IOException {
		List<FieldInfo> fieldInfos = ObjectParser.getObjectInfo(RT1.class); 
		for(FieldInfo fi : fieldInfos) {
			System.err.println(fi.getName()+": "+fi.getType());
		}
	}

}
