package com.openfms.core.service.project.impl.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsMovieVersionService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.global.FmsComment;
import com.openfms.model.core.global.FmsNotification;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;


@Component
public class CommentListener extends FmsListenerAdapter<FmsComment> {
	
	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsMovieVersionService movieVersionService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	protected CommentListener() {
	}

	@Override
	protected boolean beforeSave(String db, FmsComment comment) {
		
		comment.setAuthor(FmsSessionHolder.getCurrentUser().getName());
		comment.setAuthorUserId(FmsSessionHolder.getCurrentUser().getId());
		comment.setDate(new Date());

		try {
			Class<FmsObject> c = null;
			try {
				c = (Class<FmsObject>)Class.forName(comment.getObjectType());
			} catch (Exception e) {
				throw new RuntimeException("invalid type",e);
			}
			FmsObject object = dataStore.getObject(c, comment.getObjectId());
			if(object==null) {
				throw new RuntimeException("invalid id");
			} else if (object instanceof FmsComment) {
				comment.setName(((FmsComment)object).getRootName());
				comment.setRootId(((FmsComment)object).getRootId());
				comment.setRootType(((FmsComment)object).getRootType());
			} else {
				comment.setName(object.getName());
				comment.setRootId(object.getId());
				comment.setRootType(object.getClass().getCanonicalName());
			}
			String t = comment.getText();
			List<String> usernames = getPotentialUsernames(t);
			notifyUsers(comment,object,usernames);
		} catch (Exception e) {
			if(comment.getId()!=null) {
				try {
					dataStore.deleteObject(comment);
				} catch (Exception e2) {
				}
			}
			throw new RuntimeException("error setting comment hierarchy!");
		}
		
		return true;
		
		
	}
	
	public void notifyUsers(FmsComment comment, FmsObject object, List<String> usernames) throws DatabaseException, VersioningException {
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.in("username", usernames);
		for(FmsUser u : dataStore.findObjects(FmsProjectUser.class, q)) {
			FmsNotification n = new FmsNotification();
			n.setCommentId(comment.getId());
			n.setFrom(comment.getAuthor());
			n.setName(comment.getName());
			n.setRootObjectId(object.getId());
			n.setRootObjectType(object.getClass().getCanonicalName());
			n.setTo(u.getId());
			n.setMessage(comment.getAuthor()+" mentioned you in a comment on "+object.getName()+" saying: "+comment.getText());
			dataStore.saveObject(n);
		}
	}

	public List<String> getPotentialUsernames(String in) {
		List<String> out = new ArrayList<String>();
		if(in==null) {
			return out;
		}
		Pattern p = Pattern.compile("@[a-zA-Z0-9_]*");
		Matcher m = p.matcher(in);
		while(m.find()) {
			out.add(m.group().substring(1));
		}
		return out;
	}
	


}
