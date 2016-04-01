package com.openfms.utils.crypto.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.X509CertParser;
import org.bouncycastle.jce.provider.X509CertificateObject;

public class CertificateInfo {

	@SuppressWarnings("unchecked")
	public void parseCert(InputStream is) throws CertificateException {
		try {
			
			X509CertParser cp = new X509CertParser();
			
			cp.engineInit(is);
			
			List<X509CertificateObject> certs = new ArrayList<X509CertificateObject>();
			certs.addAll(cp.engineReadAll());
			for(X509CertificateObject cert : certs) {
				
				System.err.println(" ============================================ ");
				System.err.println("   Cert DN : "+cert.getSubjectDN().getName());
				System.err.println(" Issuer DN : "+cert.getIssuerDN().getName());
				System.err.println("    Serial : "+cert.getSerialNumber());
			}
			
			
			
		} catch (Exception e) {
			throw new CertificateException("failed to parse certificates", e);
		}
	}
	public void parseCerts(File f) throws IOException, CertificateException {
		if(f.exists()) {
			if(f.isFile()) {
				System.err.println(" ============================================ ");
				System.err.println();
				System.err.println(f.getCanonicalPath());
				System.err.println();
				parseCert(new FileInputStream(f));
			} else if(f.isDirectory()) {
				for(File fc : f.listFiles()) {
					parseCerts(fc);
				}
			}
		}
	};
	

	public void parseCerts(String s) throws IOException, CertificateException {
		File f = new File(s);
		parseCerts(f);
	}
	
	
	public static void main(String[] args) {
		CertificateInfo ci = new CertificateInfo();
		for(String s : args) {
			try {
				ci.parseCerts(s);
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
