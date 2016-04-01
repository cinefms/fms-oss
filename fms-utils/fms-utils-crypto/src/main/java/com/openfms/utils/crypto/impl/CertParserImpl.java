package com.openfms.utils.crypto.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.X509CertParser;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.openssl.PEMWriter;
import org.springframework.stereotype.Component;

import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsCertificateParseResult;
import com.openfms.model.exceptions.BasicCertificateException;
import com.openfms.model.exceptions.CertificateException;
import com.openfms.model.spi.crypto.CertParser;
import com.openfms.utils.common.IOUtils;


@Component
public class CertParserImpl implements CertParser {

	private static Log log = LogFactory.getLog(CertParserImpl.class);
	
	public static String getDnQualifier(String in) throws InvalidNameException {
		if(in==null) {
			return null;
		}
		LdapName ln = new LdapName(in);
		for (Rdn r : ln.getRdns()) {
			if(r.getType().compareTo("DN")==0) {
				return r.getValue()+"";
			}
		}
		return null;
		
	}
	
	@Override
	public byte[] createCertificateChain(List<byte[]> certificateData) throws CertificateException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer w = new OutputStreamWriter(baos);
			PEMWriter pw = new PEMWriter(w);
			X509CertParser cp = new X509CertParser();
			for(byte[] buf: certificateData) {
				cp.engineInit(new ByteArrayInputStream(buf));
				pw.writeObject(cp.engineRead());
			}
			pw.flush();
			pw.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new BasicCertificateException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FmsCertificateParseResult> parseChain(byte[] bytes) throws BasicCertificateException {
		List<FmsCertificateParseResult> out = new ArrayList<FmsCertificateParseResult>();
		try {
			
			X509CertParser cp = new X509CertParser();
			
			cp.engineInit(new ByteArrayInputStream(bytes));
			
			List<X509CertificateObject> certs = new ArrayList<X509CertificateObject>();
			certs.addAll(cp.engineReadAll());
			for(X509CertificateObject cert : certs) {

				FmsCertificate fc = new FmsCertificate();

				fc.setCertificateDn(cert.getSubjectDN().getName());
				fc.setCertificateDnQualifier(getDnQualifier(fc.getCertificateDn()));
				
				fc.setParentDn(cert.getIssuerDN().getName());
				fc.setParentDnQualifier(getDnQualifier(fc.getParentDn()));

				fc.setValidFrom(cert.getNotBefore());
				fc.setValidTo(cert.getNotAfter());
				
				fc.setSerial(cert.getSerialNumber()+"");
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Writer w = new OutputStreamWriter(baos);
				PEMWriter pw = new PEMWriter(w);
				pw.writeObject(cert);
				pw.flush();
				pw.close();
				
				FmsCertificateParseResult cpr = new FmsCertificateParseResult();
				cpr.setCertificate(fc);
				cpr.setData(baos.toByteArray());
				cpr.setOk(true);
				cpr.setDnQualifier(fc.getCertificateDnQualifier());
				out.add(cpr);
			}
		} catch (Exception e) {
			throw new BasicCertificateException(e);
		}
		return out;		
	}

	private List<byte[]> getFiles(byte[] data) {
		List<byte[]> out = new ArrayList<byte[]>();
		boolean success = false;
		try {
			if(log.isDebugEnabled()){ log.debug("trying as ZIP file .... "); }
			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data));
			ZipEntry ze;
			if(log.isDebugEnabled()){ log.debug("checking entries ... "); }
			while((ze = zis.getNextEntry())!=null) {
				if(ze.isDirectory() || ze.getName().endsWith(".DS_Store")) {
					continue;
				}
				if(log.isDebugEnabled()){ log.debug("checking entries ... "+ze.getSize()+" / "+ze.getName()); }
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
				IOUtils.copy(zis, baos);
				byte[] b = baos.toByteArray();
				out.add(baos.toByteArray());
				success = true;
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()){ log.debug("result from ZIP",e); }
		}
		if(!success) {
			if(log.isDebugEnabled()){ log.debug("checking non-zip"); }
			out.add(data);
		}
		return out;
	}
	
	
	
	@Override
	public List<FmsCertificateParseResult> parseCerts(byte[] data) throws CertificateException {
		List<FmsCertificateParseResult> out = new ArrayList<FmsCertificateParseResult>();
		for(byte[] buff : getFiles(data)) {
			try {
				out.addAll(parseChain(buff));
			} catch (Exception e) {
			}
		}
		return out;
	}
	
}
