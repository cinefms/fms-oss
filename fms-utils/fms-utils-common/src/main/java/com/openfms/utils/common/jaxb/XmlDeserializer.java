package com.openfms.utils.common.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

public class XmlDeserializer {

	
	public static <T> T read(Class<T> c, InputStream is) throws IOException {
        try {
			JAXBContext context = JAXBContext.newInstance(c);
	        Unmarshaller marsh = context.createUnmarshaller();
	        T t = (T)marsh.unmarshal(is);
	        return t;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static <T> String write(T t) throws IOException {
        try {
			JAXBContext context = JAXBContext.newInstance(t.getClass());
	        JAXBElement<T> msg = new JAXBElement(new QName(t.getClass().getSimpleName()), t.getClass(), t);
	        Marshaller marsh = context.createMarshaller();
	        StringWriter writer = new StringWriter();
	        marsh.marshal(msg, writer);
	        return writer.toString();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
