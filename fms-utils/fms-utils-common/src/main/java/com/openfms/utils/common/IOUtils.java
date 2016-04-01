package com.openfms.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.openfms.utils.common.md5.MD5;
import com.openfms.utils.common.md5.MD5OutputStream;
import com.openfms.utils.common.md5.NullOutputStream;


public class IOUtils {
	
	public static long copy(InputStream is, OutputStream os) throws IOException {
		byte[] buff = new byte[1024*1024];
		int a = 0;
		long o = 0;
		while((a = is.read(buff)) > -1) {
			os.write(buff,0,a);
			o = o + a;
		}
		os.flush();
		return o;
	}
	
	public static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(is,baos);
		return baos.toByteArray();
	}
	
	
	public static byte[] getBytes(File f) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			return getBytes(fis);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
			}
		}
	}

	public static MD5 getMd5(byte[] buff) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(buff);
		return getMd5(bais);
	}
	
	public static MD5 getMd5(InputStream is) throws IOException {
		MD5OutputStream mos = new MD5OutputStream(new NullOutputStream());
		IOUtils.copy(is, mos);
		mos.flush();
		mos.close();
		return mos.getMD5();
	}
	
	public static MD5 getMd5(File f) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			return getMd5(fis);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
			}
		}
	}
	
	

}
