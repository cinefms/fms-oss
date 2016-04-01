package com.openfms.utils.common.mail;

import java.util.Properties;

import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailUtil {
	
	private static Log log = LogFactory.getLog(MailUtil.class);

	
	
	
	
	public static Session createMailSession(String username, String password, String sender, String host, int port, boolean useSSL, boolean useTLS, boolean requireTLS) {

		Properties props = System.getProperties();
		
		props.put("mail.transport.protocol", "smtp");
		if(sender!=null) {
			props.put("mail.smtp.from", sender);
		}
		props.put("mail.smtp.host", host);

		if (useSSL) {
			props.put("mail.smtp.port", port<1?443:port);
		} else {
			props.put("mail.smtp.starttls.enable", useTLS);
			props.put("mail.smtp.starttls.require", requireTLS);
			props.put("mail.smtp.port", port<1?25:port);
		}
		if(log.isDebugEnabled()){ log.debug("sending mail using SMTP host: "+props.getProperty("mail.smtp.host")+":"+props.getProperty("mail.smtp.port")+" / TLS:"+props.getProperty("mail.smtp.starttls")+" / SSL:"+props.getProperty("mail.smtp.ssl")); }
		if (username != null) {
			props.put("mail.smtp.auth", "true");
			if(log.isDebugEnabled()){ log.debug("sending mail WITH SMTP auth ... "); }
			return Session.getInstance(props, new PasswordAuthenticator(username, password));
		} else {
			if(log.isDebugEnabled()){ log.debug("sending mail WITHOUT SMTP auth ... "); }
			return Session.getInstance(props);
		}
		
	}

}
