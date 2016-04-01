package com.openfms.core.service.project.impl.listeners;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.core.service.project.FmsMediaClipTaskTypeService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.core.service.project.FmsProjectUserService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.FmsProjectUser;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.global.FmsNotification;
import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.core.movie.FmsMediaClipTask;
import com.openfms.model.core.movie.FmsMediaClipTaskProgress;
import com.openfms.model.core.movie.FmsMediaClipTaskProgressFollowUp;
import com.openfms.model.core.movie.FmsMediaClipTaskType;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.utils.common.md5.MD5;

@Component
public class MediaClipTaskProgressListener extends FmsListenerAdapter<FmsMediaClipTaskProgress> {

	private static Log log = LogFactory.getLog(MediaClipTaskProgressListener.class);

	@Autowired
	private ProjectDataStore dataStore;

	@Autowired
	private FmsMediaClipService mediaClipService;

	@Autowired
	private FmsMediaClipTaskTypeService taskTypeService;

	@Autowired
	private FmsMediaClipTaskService taskService;

	@Autowired
	private FmsProjectUserService projectUserService;

	@Autowired
	private FmsPlaybackDeviceService deviceService;
	
	private boolean needsSaving(FmsMediaClipTask task, FmsMediaClipTaskProgress progress) {
		if(task==null) {
			return false;
		}
		if(task.isClosed() && progress.getClose()==null) {
			return false;
		}
		if(progress.getId()!=null) {
			return false;
		}
		if(progress.getClose()!=null) {
			return true;
		}
		if(progress.getPriority()!=null) {
			return true;
		}
		if(progress.getDisable()!=null) {
			return true;
		}
		if(progress.getStatus()!=null) {
			return true;
		}
		if(progress.getDueDate()!=null) {
			return true;
		}
		if((progress.getAssignTo()+"").compareTo(task.getUserId()+"")!=0) {
			return true;
		}
		if((progress.getDeviceId()+"").compareTo(task.getDeviceId()+"")!=0) {
			return true;
		}
		if(progress.getComment()!=null && progress.getComment().trim().length()>0) {
			return true;
		}
		if(progress.getFollowUps()!=null) {
			for(FmsMediaClipTaskProgressFollowUp fup : progress.getFollowUps()) {
				if(fup.isCreate()) {
					return true;
				}
			}
		} 
		return false;
	}
	
	@Override
	protected boolean beforeSave(String db, FmsMediaClipTaskProgress progress) {
		progress.setDate(new Date());
		progress.setUser(FmsSessionHolder.getCurrentUser().getName());
		progress.setUserId(FmsSessionHolder.getCurrentUser().getId());
		try {
			
			FmsMediaClipTask task = taskService.get(progress.getMediaClipTaskId());

			if(!needsSaving(task, progress)) {
				return false;
			}
			
			FmsMediaClip clip = mediaClipService.get(task.getMediaClipId());

			if (progress.getDisable() != null) {
				log.info("clip set to disabled: " + clip.isDisabled() +" ---> "+progress.getDisable());
				clip.setDisabled(progress.getDisable());
			}
			if (progress.getStatus() != null) {
				if(log.isDebugEnabled()){ log.debug("clip status set tp: " + clip.getStatus()+" ---> "+progress.getStatus()); }
				log.info("clip status set tp: " + clip.getStatus()+" ---> "+progress.getStatus());
				clip.setStatus(progress.getStatus());
			}

			log.info("clip status before save: " + clip.getStatus());
			clip = mediaClipService.save(clip);
			log.info("clip status after save: " + clip.getStatus());
			
			// reload & update the task
			log.info("task media clip status before save: " + task.getMediaClipStatus());
			task = taskService.get(progress.getMediaClipTaskId());			
			log.info("task media clip status after save: " + task.getMediaClipStatus());

			if(progress.getDueDate()!=null) {
				if(progress.getDueDate().getTime()==0) {
					task.setDueDate(null);
				} else {
					task.setDueDate(progress.getDueDate());
				}
			}
			
			if (progress.getClose() != null) {
				log.info((progress.getClose().booleanValue() ? "CLOSING " : "REOPENING ") + " TASK: " + task.getId());
				task.setClosed(progress.getClose());
			}
			if (progress.getAssignTo() != null) {
				FmsProjectUser user = projectUserService.get(progress.getAssignTo());
				progress.setAssignToUserName(user.getName());
				task.setUserId(user.getId());
				task.setUserName(user.getName());
			} else {
				task.setUserId(null);
				task.setUserName(null);
			}
			if(progress.getDeviceId()!=null) {
				FmsPlaybackDevice d = deviceService.get(progress.getDeviceId());
				log.info("setting device in task: "+d.getId()+" / "+d.getDisplayName());
				task.setDeviceId(d.getId());
				task.setDeviceName(d.getDisplayName());
			} else {
				log.info("unsetting device in task: ");
				task.setDeviceId(null);
				task.setDeviceName(null);
			}
			if (progress.getPriority() != null) {
				log.debug("task priority set to: " + progress.getPriority());
				task.setPriority(progress.getPriority());
			}
			if(progress.getFollowUps()!=null) {
				
				for(FmsMediaClipTaskProgressFollowUp fup : progress.getFollowUps()) {

					if(fup.isCreate()) {
						
						FmsMediaClipTask newTask = new FmsMediaClipTask();
						newTask.setPreviousTaskId(progress.getMediaClipTaskId());
						newTask.setComment(fup.getComment());
						newTask.setPreviousTaskType(task.getType());
						newTask.setPriority(fup.getPriority());
						newTask.setStatus(fup.getStatus());
						newTask.setClosed(false);
						newTask.setMediaClipId(task.getMediaClipId());
						
						FmsMediaClipTaskType tt = taskTypeService.get(fup.getType());
						newTask.setType(tt.getId());
						newTask = taskService.save(newTask);

						String deviceId = null;
						if(tt.getDeviceId()!=null) {
							deviceId = tt.getDeviceId();
						} 
						if(fup.getDeviceId()!=null) {
							deviceId = fup.getDeviceId();
						}
						if(deviceId!=null) {
							try {
								FmsPlaybackDevice d = deviceService.get(deviceId);
								newTask.setDeviceId(d.getId());
								newTask.setDeviceName(d.getDisplayName());
							} catch (Exception e) {
								log.error("error setting device name/id as derived data",e);
							}
							
						}
						
						fup.setCreated(newTask.getId());
						
						taskService.save(newTask);
						
					}
				}
			}

			log.info(" saving task (again) ... ");
			task = dataStore.saveObject(task, true, true);
			log.info(" saving task (again) ... media clip status is: "+task.getMediaClipStatus());

			List<String> potentialUsernames = getPotentialUsernames(progress.getComment());
			notifyUsers(progress, task, potentialUsernames);

		} catch (Exception e) {
			log.error("error saving task progress: ",e);
			throw new RuntimeException(e);
		}
		return true;

	}

	public void notifyUsers(FmsMediaClipTaskProgress comment, FmsObject object, List<String> usernames) throws DatabaseException, VersioningException {
		DBStoreQuery q = BasicQuery.createQuery();
		q = q.in("username", usernames);
		List<FmsProjectUser> users = dataStore.findObjects(FmsProjectUser.class, q);
		if(log.isDebugEnabled()){ log.debug("notifying " + users.size() + " / " + usernames.size() + " users (rest ignored)"); }
		for (FmsUser u : users) {
			if(log.isDebugEnabled()){ log.debug("notifying " + users.size() + " / " + usernames.size() + " users: " + u.getName()); }
			FmsNotification n = new FmsNotification();
			n.setCommentId(comment.getId());
			n.setName(object.getName());
			n.setFrom(comment.getUser());
			n.setRootObjectId(object.getId());
			n.setRootObjectType(object.getClass().getCanonicalName());
			n.setTo(u.getId());
			n.setMessage(comment.getUser() + " mentioned you in a comment on a task saying: " + comment.getComment());
			dataStore.saveObject(n);
		}
	}

	public List<String> getPotentialUsernames(String in) {
		List<String> out = new ArrayList<String>();
		if (in == null) {
			return out;
		}
		Pattern p = Pattern.compile("@[a-zA-Z0-9_]*");
		Matcher m = p.matcher(in);
		while (m.find()) {
			out.add(m.group().substring(1));
		}
		return out;
	}

	@Override
	protected void created(String db, FmsMediaClipTaskProgress object) {
		try {
			
		} catch (Exception e) {
			throw new RuntimeException("error creating followups" ,e );
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String a = "1453976859414";
		String b = "1321157";
		String x = a+":"+b;
		byte[] bytesOfMessage = x.getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] thedigest = md.digest(bytesOfMessage);
		for(byte bb : thedigest) {
			System.out.printf("%02X ", bb);
		}
	}
	
}
