package com.openfms.utils.crypto.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyParseResult;
import com.openfms.model.spi.crypto.KeyParser;
import com.openfms.utils.common.IOUtils;
import com.openfms.utils.common.text.XmlUtil;
import com.openfms.utils.crypto.AbstractDCIParser;
import com.openfms.utils.crypto.DnUtil;

@Component
public class KdmParserImpl extends AbstractDCIParser implements KeyParser {

	private static Log log = LogFactory.getLog(KdmParserImpl.class);

	@Override
	public boolean isValidKDM(byte[] data) {
		if(log.isDebugEnabled()){ log.debug("checking as KDM: "+data.length+" bytes of data ... "); }
		return isXML(data, "DCinemaSecurityMessage");
	}
	
	public List<FmsKeyParseResult> getFiles(String filename, byte[] data) {
		List<FmsKeyParseResult> out = new ArrayList<FmsKeyParseResult>();
		try {
			if(log.isDebugEnabled()){ log.debug("trying as ZIP file .... "); }
			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data));
			ZipEntry ze;
			if(log.isDebugEnabled()){ log.debug("checking entries ... "); }
			boolean success = false;
			while((ze = zis.getNextEntry())!=null) {
				if(ze.isDirectory() || ze.getName().endsWith(".DS_Store")) {
					continue;
				}
				if(log.isDebugEnabled()){ log.debug("checking entries ... "+ze.getSize()+" / "+ze.getName()); }
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				IOUtils.copy(zis, baos);
				byte[] b = baos.toByteArray();
				FmsKeyParseResult r = new FmsKeyParseResult();
				r.setFilename(ze.getName());
				r.setData(b);
				out.add(r);
				success = true;
			}
			if(success) {
				return out;
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()){ log.debug("result from ZIP",e); }
		}
		if(log.isDebugEnabled()){ log.debug("checking non-zip"); }
		FmsKeyParseResult r = new FmsKeyParseResult();
		r.setFilename(filename);
		r.setData(data);
		out.add(r);
		return out;
	}
	
	

	@Override
	public List<FmsKeyParseResult> parseKdms(String source, String filename, String movieId, byte[] data) throws IOException {
		
		List<FmsKeyParseResult> out = getFiles(filename, data);
		
		if(log.isDebugEnabled()){ log.debug("found "+out.size()+" files to parse ... "); }
		
		for(FmsKeyParseResult r : out) {
			r.setOk(false);
			if(isValidKDM(r.getData())) {
				if(log.isDebugEnabled()){ log.debug("found "+out.size()+" files to parse ... file "+r.getFilename()+" is a KDM"); }
				try {
					FmsKey k = parseKdm(source, r.getFilename(), movieId, r.getData());
					r.setKey(k);
					r.setOk(true);
				} catch (Exception e) {
				}
			} else {
				if(log.isDebugEnabled()){ log.debug("found "+out.size()+" files to parse ... file "+r.getFilename()+" is NOT a KDM"); }
			}
		}
		
		return out;
	}

	@Override
	public FmsKey parseKdm(String source, String filename, String movieId, byte[] data) throws IOException {
		FmsKey out = null;

		try {
			Document d = XmlUtil.parse(data, false);
			out = new FmsKey();
			
			List<Node> annotationText = XmlUtil.getNodes(null, d, "//AuthenticatedPublic/AnnotationText");
			if (annotationText.size() > 0) {
				out.setName(annotationText.get(0).getTextContent());
			}

			
			out.setName(filename);
			out.setSource(source);
			
			String dn = getSingleXPathResult(d, "//Recipient/X509SubjectName");
			out.setCertificateDn(dn);
			out.setCertificateDnQualifier(DnUtil.getDnComponent(dn,"dnQualifier"));

			String issuerDn = getSingleXPathResult(d, "//Signer/X509IssuerName");
			out.setIssuerDn(issuerDn);
			out.setIssuerDnQualifier(DnUtil.getDnComponent(issuerDn,"dnQualifier"));

			out.setExternalId(getUuid(getSingleXPathResult(d, "//AuthenticatedPublic/MessageId")));
			out.setValidFrom(parseDate(getSingleXPathResult(d, "//ContentKeysNotValidBefore")));
			out.setValidTo(parseDate(getSingleXPathResult(d, "//ContentKeysNotValidAfter")));
			out.setmediaClipExternalId(getUuid(getSingleXPathResult(d, "//CompositionPlaylistId")));
			
					
			List<String> x = getMultiXPathResult(d, "//AuthorizedDeviceInfo/DeviceList/CertificateThumbprint");
			if(x!=null && x.size()>0) {
				x.remove("2jmj7l5rSw0yVb/vlWAYkK/YBwk=");
			}
			out.setTdlThumbprints(x);
			
			out.setMd5(IOUtils.getMd5(data).asHex());
			out.setType("KDM");
			out.setMovieId(movieId);
			
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("error parsing KDM", e);
		}

	}


	private static String getUuid(String nodeValue) {
		if(nodeValue == null) {
			return "";
		}
		if(nodeValue.indexOf(":")>-1) {
			nodeValue = nodeValue.substring(nodeValue.lastIndexOf(":"));
		}
		return nodeValue.replaceAll("[^\\dA-Fa-f]*","");
	}

	
	
}
