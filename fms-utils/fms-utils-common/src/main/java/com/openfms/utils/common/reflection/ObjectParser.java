package com.openfms.utils.common.reflection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public class ObjectParser {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	@SuppressWarnings("unchecked")
	private static List<FieldInfo> getFieldInfos(String name, Object o) {
		List<FieldInfo> out = new ArrayList<FieldInfo>();
		if(o==null) {
		} else if(o instanceof Map) {

			Map m = (Map)o;

			String x = m.get("type").toString(); 
			
			if(x.toString().compareTo("object")==0) {
				if(m.get("properties")!=null) {
					Map<String,Object> mp = (Map<String,Object>) m.get("properties");
					for(String s : mp.keySet()) {
						String n = s;
						if(name!=null) {
							n = name+"."+s;
						}
						out.addAll(getFieldInfos(n, mp.get(s)));
					}
				}
			} else if(x.compareTo("array")==0) {
				out.addAll(getFieldInfos(name, m.get("items")));
			} else if(x.compareTo("string")==0) {
				FieldInfo fi = new FieldInfo();
				fi.setName(name);
				fi.setType(String.class);
				out.add(fi);
			} else if(x.compareTo("integer")==0) {
				FieldInfo fi = new FieldInfo();
				fi.setName(name);
				fi.setType(Number.class);
				out.add(fi);
			}
		}
		return out;
	}
	
	public static List<FieldInfo> getObjectInfo(Class<?> clazz) throws IOException {
		ObjectMapper m = new ObjectMapper();
		SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		m.acceptJsonFormatVisitor(m.constructType(clazz), visitor);
		JsonSchema schema = visitor.finalSchema().asSimpleTypeSchema();
		byte[] bx = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(schema);
		System.err.write(bx);
		Map<String,Object> out = objectMapper.readValue(bx,Map.class);
		List<FieldInfo> fis = getFieldInfos(null, out);
		Collections.sort(fis);
		return fis;
	}
	

}
