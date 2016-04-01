package com.openfms.core.service.project.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.project.FmsMailService;
import com.openfms.core.service.project.FmsMailTemplateService;
import com.openfms.core.service.project.ProjectSettingService;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.config.FmsCredentials;
import com.openfms.model.core.project.FmsMailServerConfig;
import com.openfms.model.core.project.FmsMailTemplate;
import com.openfms.model.core.project.FmsProjectHolder;
import com.openfms.model.core.project.FmsProjectSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.utils.common.freemarker.FreemarkerUtil;
import com.openfms.utils.common.mail.MailUtil;
import com.sun.mail.smtp.SMTPMessage;

@Service
public class FmsProjectSettingsAndMailServiceImpl implements ProjectSettingService, FmsMailTemplateService, FmsMailService {

	private static Log log = LogFactory.getLog(FmsProjectSettingsAndMailServiceImpl.class);
	
	@Autowired
	private AuthzService authzService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	
	@Override
	public FmsProjectSettings getSettings() throws AccessDeniedException, DatabaseException, VersioningException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		FmsProjectSettings out = dataStore.getObject(FmsProjectSettings.class, FmsProjectHolder.get().getId());
		if(out == null) {
			out = new FmsProjectSettings();
			out.setId(FmsProjectHolder.get().getId());
			out = dataStore.saveObject(out);
		}
		return out;
	}

	@Override
	public FmsProjectSettings saveSettings(FmsProjectSettings settings) throws DatabaseException, VersioningException, AccessDeniedException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		settings.setId(FmsProjectHolder.get().getId());
		return dataStore.saveObject(settings);
	}

	
	
	
	@Override
	public FmsMailServerConfig getMailServerConfig() throws DatabaseException, AccessDeniedException, VersioningException {
		if(!authzService.allowAccess(FmsMailServerConfig.class, null, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		return getMailServerConfigInternal();
	}
		
	private FmsMailServerConfig getMailServerConfigInternal() throws DatabaseException {
		FmsMailServerConfig out = dataStore.getObject(FmsMailServerConfig.class, FmsProjectHolder.get().getId());
		if(out == null) {
			out = new FmsMailServerConfig();
			out.setId(FmsProjectHolder.get().getId());
			try {
				out = dataStore.saveObject(out);
			} catch (Exception e) {
				throw new DatabaseException(e);
			}
		}
		return out;
	}

	@Override
	public FmsMailServerConfig saveMailServerConfig(FmsMailServerConfig config) throws DatabaseException, VersioningException, AccessDeniedException {
		if(!authzService.allowAccess(FmsMailServerConfig.class, null, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		config.setId(FmsProjectHolder.get().getId());
		return dataStore.saveObject(config);
	}

	@Override
	public void saveCredentials(FmsCredentials c) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException {
		if(!authzService.allowAccess(FmsMailServerConfig.class, null, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		FmsMailServerConfig config = getMailServerConfig();
		config.setUsername(c.getUsername());
		saveMailServerConfig(config);
		
		c.setId(FmsProjectHolder.get().getId());
		c = dataStore.saveObject(c);
		if(c.getUsername()==null || c.getUsername().length()==0) {
			dataStore.deleteObject(c);
		}
	}
	
	private FmsCredentials getCredentials() throws DatabaseException {
		return dataStore.getObject(FmsCredentials.class, FmsProjectHolder.get().getId());
	}

	@Override
	public void sendMail(String mail, Map<String, Object> tokens, String... recipients) throws DatabaseException, EntityNotFoundException, IOException {
		
		if(mail==null) {
			throw new RuntimeException("please tell me what mail to send ... ");
		}
		
		if(recipients==null || recipients.length==0) {
			throw new RuntimeException("please tell me who to send to ... ");
		}
		
		FmsMailTemplate t = getByNameInternal(mail);
		FmsMailServerConfig c = getMailServerConfigInternal();
		FmsCredentials fc = getCredentials();
		
		if(log.isDebugEnabled()){ log.debug(" --- sending mail: "+mail); }
		if(log.isDebugEnabled()){ log.debug(" --- sending mail: credentials are: "+(fc==null?"[NULL]":fc.getUsername()+"/xxxxxx")); }
		if(log.isDebugEnabled()){ log.debug(" --- sending mail: mail server config is: "+(c==null?"[NULL]":c.getHost())); }
		if(log.isDebugEnabled()){ log.debug(" --- sending mail: sender is config is: "+(c==null?"[NULL]":c.getHost())); }
		
		Session mailSession = MailUtil.createMailSession(
				fc!=null?fc.getUsername():null, 
				fc!=null?fc.getPassword():null, 
				t.getSender(),
				c.getHost(),
				c.getPort(),
				c.isUseSsl(),
				c.isUseTls(),
				true);

		for(String recipient : recipients) {
			
			try {
				
				SMTPMessage mm = new SMTPMessage(mailSession);
				InternetAddress senderAddr = new InternetAddress(t.getSender());
				InternetAddress recipientAddr = new InternetAddress(recipient);
				mm.setRecipient(javax.mail.Message.RecipientType.TO, recipientAddr);
				mm.setSender(senderAddr);
				mm.setFrom(senderAddr);
				mm.setEnvelopeFrom(t.getSender());
				mm.setHeader("Content-Type", "text/plain; charset=utf-8");
				mm.setHeader("Content-Transfer-Encoding", "quoted-printable");
				mm.setSubject(FreemarkerUtil.process(t.getSubject(), tokens));
				
				MimeMultipart mmpt = new MimeMultipart("alternative");
				MimeBodyPart bpText = new MimeBodyPart();
				bpText.setText(FreemarkerUtil.process(t.getBody(), tokens));
				bpText.setHeader("Content-Type", "text/plain; charset=utf-8");
				bpText.setHeader("Content-Transfer-Encoding", "quoted-printable");
				mmpt.addBodyPart(bpText);
				
				mm.setContent(mmpt);
				mm.saveChanges();
				
				System.err.println("--------------------------------------------------------------------- ");
				mm.writeTo(System.err);
				System.err.println("--------------------------------------------------------------------- ");
				
				Transport.send(mm);
				
			} catch (Exception e) {
				throw new IOException(e);
			}
		
		}		
	}

	@Override
	public List<FmsMailTemplate> list(DBStoreQuery query) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		return dataStore.findObjects(FmsMailTemplate.class, query);
	}

	@Override
	public FmsMailTemplate get(String templateId) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		return dataStore.getObject(FmsMailTemplate.class, templateId);
	}

	@Override
	public FmsMailTemplate getByName(String name) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.READ)) {
			throw new AccessDeniedException();
		}
		return getByNameInternal(name);
		
	}
	
	private FmsMailTemplate getByNameInternal(String name) throws DatabaseException, EntityNotFoundException {
		DBStoreQuery q = BasicQuery.createQuery().eq("name", name);
		return dataStore.findObject(FmsMailTemplate.class,q);
	}

	@Override
	public FmsMailTemplate save(FmsMailTemplate template) throws AccessDeniedException, DatabaseException, VersioningException, EntityNotFoundException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.UPDATE)) {
			throw new AccessDeniedException();
		}
		FmsMailTemplate t = getByName(template.getName());
		if(t!=null) {
			template.setId(t.getId());
		}
		return dataStore.saveObject(template);
	}

	@Override
	public void delete(String templateId) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.DELETE)) {
			throw new AccessDeniedException();
		}
		dataStore.deleteObject(FmsMailTemplate.class,templateId);
	}
	
	@Override
	public void sendTest(String id, String recipient, Map<String, Object> params) throws AccessDeniedException, DatabaseException, EntityNotFoundException, IOException {
		if(!authzService.allowAccess(FmsProjectSettings.class, null, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		FmsMailTemplate t = get(id);
		
		System.err.println("PARAMS: "+(new ObjectMapper().writeValueAsString(params)));
		
		sendMail(t.getName(), params, recipient);
	}
	
	
}
