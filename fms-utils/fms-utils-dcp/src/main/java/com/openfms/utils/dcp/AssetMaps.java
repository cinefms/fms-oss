package com.openfms.utils.dcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.ls.LSResourceResolver;

import com.openfms.utils.dcp.generic.AssetMap;
import com.openfms.utils.dcp.generic.Type;

public class AssetMaps {

	static boolean validateAgainstXSD(InputStream xml, String xsd) throws IOException {
	    try {

	    	SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        LSResourceResolver resolver = new LocalSchemaResolver();
			//factory.setResourceResolver(resolver);
	    	InputStream is = AssetMaps.class.getClassLoader().getResourceAsStream(xsd);
	        Schema schema = factory.newSchema(new StreamSource(is));
            Validator validator = schema.newValidator();
	    	
	    	DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
	    	
	    	// stop the network loading of DTD files
	    	df.setValidating(false);
	    	df.setNamespaceAware(true);
	    	df.setSchema(schema);
	    	df.setFeature("http://xml.org/sax/features/namespaces", true);
	    	df.setFeature("http://xml.org/sax/features/validation", false);
	    	df.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	    	df.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

	    	// open up the xml document
	    	DocumentBuilder docbuilder = df.newDocumentBuilder();
	    	Document docXml = docbuilder.parse(xml);
	    	
            validator.validate(new DOMSource(docXml));
	    	
	        return true;
	    } catch(Exception ex) {
	    	throw new IOException("erro parsing xml",ex);
	    }
	}	
	
	
	
	public static AssetMap parseAssetMap(File file) {
		AssetMap out = new AssetMap();
		System.err.println(" ============================================= "+file.getAbsolutePath());
		try {
			System.err.println("interop");
			validateAgainstXSD(new FileInputStream(file),"schema/dcp/interop/PROTO-ASDCP-AM-20040311.xsd");
			out.setType(Type.INTEROP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.err.println("smpte");
			validateAgainstXSD(new FileInputStream(file),"schema/dcp/smpte/SMPTE-429-8-2006-PKL.xsd");
			out.setType(Type.SMPTE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println(out.getType());
		return out;
	}
	
	public static void main(String[] args) {
		parseAssetMap(new File("/Users/rm/Eclipse/fms/mount/test_framerate_24fps/ASSETMAP.xml"));
		parseAssetMap(new File("/Volumes/2TA/eisenstein_a/ASSETMAP"));
	}
	
	
}
