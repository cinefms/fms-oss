package com.openfms.utils.crypto.impl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.springframework.util.Base64Utils;

public class CertificateThumbprint {

	public static String getThumbprint(byte[] certtData) throws CertificateException, NoSuchAlgorithmException {
		return getThumbprint(new ByteArrayInputStream(certtData));
	}
	
	
	public static String getThumbprint(InputStream is) throws CertificateException, NoSuchAlgorithmException {
		 CertificateFactory x509CertFact = CertificateFactory.getInstance("X.509");
         X509Certificate cert = (X509Certificate)x509CertFact.generateCertificate(is);
         MessageDigest md = MessageDigest.getInstance("SHA-1");
         byte[] der = cert.getTBSCertificate();
         md.update(der);
         byte[] sha1digest = md.digest();
         return Base64Utils.encodeToString(sha1digest);
	}
	
	
	public static void main(String[] args) throws CertificateException, NoSuchAlgorithmException, FileNotFoundException {
		System.err.println(getThumbprint(new FileInputStream(args[0])));
	}
	
	
}
