package com.openfms.utils.common.text;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

public class StringUtil {

	public static String getStackTrace(Throwable e) {
		StringBuffer sb = new StringBuffer();
		while(e!=null) {
			sb.append(e.getClass()+" / "+e.getMessage()+"\n");
			sb.append(getStackTrace(e.getStackTrace()));
			sb.append("caused by ... \n");
			e = e.getCause();
		}
		return sb.toString();
	}

	public static String readableSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static String formatByteArray(byte[] in) {
		int count = 0;
		StringBuffer out = new StringBuffer();
		for (byte b : in) {
			count += 1;
			out.append(String.format(" %0$2x", b));
			if (count % 32 == 0) {
				out.append("\r");
			} else if (count % 8 == 0) {
				out.append("   -   ");
			} else if (count % 4 == 0) {
				out.append(" - ");
			}
		}
		return out.toString();
	}

	public static String getStackTrace(StackTraceElement[] stack) {
		StringBuffer out = new StringBuffer();
		for(StackTraceElement ste : stack) {
			out.append("\t\t at "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+" "+ste.getLineNumber()+")");
			out.append("\n");
		}
		return out.toString();
	}
	
	public static String getCurrentStack() {
		return getStackTrace(Thread.currentThread().getStackTrace());
	}

}
