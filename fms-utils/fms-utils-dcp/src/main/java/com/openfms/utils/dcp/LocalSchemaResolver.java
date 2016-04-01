package com.openfms.utils.dcp;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class LocalSchemaResolver implements LSResourceResolver {

	private static Map<String,String> schemas = new HashMap<String, String>();
	
	static {
		
		schemas.put("http://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd", "schema/other/REC-xmldsig-core-20020212.xsd");
		schemas.put("http://www.digicine.com/PROTO-ASDCP-CPL-20040511.xsd", "schema/dcp/interop/PROTO-ASDCP-CPL-20040511.xsd");
		schemas.put("http://www.digicine.com/PROTO-ASDCP-AM-20040311.xsd", "schema/dcp/interop/PROTO-ASDCP-AM-20040311.xsd");
		
	}
	
	
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		System.err.println(" - "+ type+" - "+ namespaceURI+" - "+ publicId+" - "+ systemId+" - "+ baseURI);
		String uri = baseURI;
		if(uri==null) {
			uri = systemId;
		}
		if(schemas.get(uri)!=null) {
	    	DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
	    	// stop the network loading of DTD files
	    	df.setValidating(false);
	    	df.setNamespaceAware(true);
	    	try {
	    		System.err.println("returning: "+uri+" ---> "+schemas.get(uri));
	    		return new LSInputImpl(systemId, publicId, baseURI, "utf-8", this.getClass().getClassLoader().getResourceAsStream(schemas.get(uri)));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
		}
		return null;
	}
	
	
	private class LSInputImpl implements LSInput {
		
		private String systemId,publicId,baseURI,encoding;
		private InputStream is;
		
		
		private LSInputImpl(String systemId, String publicId, String baseURI, String encoding, InputStream is) {
			super();
			this.systemId = systemId;
			this.publicId = publicId;
			this.baseURI = baseURI;
			this.encoding = encoding;
			this.is = is;
		}

		
		
		public String getSystemId() {
			return systemId;
		}

		public void setSystemId(String systemId) {
			this.systemId = systemId;
		}

		public String getPublicId() {
			return publicId;
		}

		public void setPublicId(String publicId) {
			this.publicId = publicId;
		}

		public String getBaseURI() {
			return baseURI;
		}

		public void setBaseURI(String baseURI) {
			this.baseURI = baseURI;
		}

		public String getEncoding() {
			return encoding;
		}

		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}

		@Override
		public Reader getCharacterStream() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCharacterStream(Reader characterStream) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public InputStream getByteStream() {
			return is;
		}

		@Override
		public void setByteStream(InputStream byteStream) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getStringData() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setStringData(String stringData) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean getCertifiedText() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setCertifiedText(boolean certifiedText) {
			// TODO Auto-generated method stub
			
		}
		

	}

}
