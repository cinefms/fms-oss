package com.openfms.core.service.admin.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.AuthzService;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.admin.ProjectService;
import com.openfms.core.service.admin.impl.auth.FmsAdminUser;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsEventService;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.core.service.project.FmsMediaClipService;
import com.openfms.core.service.project.FmsMediaClipTaskService;
import com.openfms.core.service.project.FmsMovieService;
import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsSession;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.auth.FmsUser;
import com.openfms.model.core.global.FmsUpdateResult;
import com.openfms.model.core.project.FmsProject;
import com.openfms.model.core.project.FmsProjectSettings;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.NotAuthorizedException;
import com.openfms.model.exceptions.VersioningException;
import com.openfms.utils.common.text.StringUtil;

@Component
public class ProjectServiceImpl implements ProjectService {

	private static Log log = LogFactory.getLog(ProjectServiceImpl.class);
	
	@Autowired
	private AuthzService authzService;
	
	@Autowired
	private AdminDataStore adminDataStore;
	
	@Autowired
	private FmsMediaClipTaskService mediaClipTaskService;
	
	@Autowired
	private FmsMediaClipService mediaClipService;
	
	@Autowired
	private FmsEventService eventService;
	
	@Autowired
	private FmsMovieService movieService;
	
	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	@Autowired
	private FmsCryptoKeyService keyService;
	
	@Autowired
	private FmsCryptoCertificateService certificateService;
	
	@Autowired
	private FmsKeyRequestService keyRequestService;

	@Autowired
	private ProjectDataStore dataStore;
	
	
	@PostConstruct
	public void init() {
		
		try {
			List<FmsProject> projects = adminDataStore.findObjects(FmsProject.class, BasicQuery.createQuery());
			if(projects.size()==0) {
				log.info(" #### NO PROJECT EXISTS ... CREATING DEFAULT PROJECT!");
				FmsProject project = new FmsProject();
				project.getHostnames().add("127.0.0.1");
				project.getHostnames().add("localhost");
				project.setName("Internal");
				project.setShortName("int");
				project.setActive(true);
				log.info(" #### NO PROJECT EXISTS ... CREATING DEFAULT PROJECT! SAVING ... ");
				adminDataStore.saveObject(project);
				log.info(" #### NO PROJECT EXISTS ... CREATING DEFAULT PROJECT! SAVING ... DONE!");
			}
		} catch (Exception e) {
			log.error("error checking existing projects: ",e);
		}
	}
	
	@Override
	public FmsProject getProjectForHost(String host) throws DatabaseException {
		DBStoreQuery q = BasicQuery.createQuery().in("hostnames", new String[] { host });
		FmsProject p = adminDataStore.findObject(FmsProject.class,q);	
		return p;
	}

	@Override
	public List<FmsProject> getProjects(String searchTerm, Integer start, Integer max) throws DatabaseException, AccessDeniedException {
		if(!authzService.allowAccess(FmsProject.class, AccessType.ADMIN)) {
			throw new NotAuthorizedException(FmsProject.class);
		}
		DBStoreQuery q = BasicQuery.createQuery();
		if(searchTerm!=null) {
			q = q.or(
					BasicQuery.createQuery().contains("name", searchTerm),
					BasicQuery.createQuery().contains("hostnames", searchTerm),
					BasicQuery.createQuery().contains("shortName", searchTerm)
			);
		}
		if(start!=null) {
			q = q.start(start);
		}
		if(max!=null) {
			q = q.max(max);
		}
		return adminDataStore.findObjects(FmsProject.class, q);
	}

	@Override
	public FmsProject getProject(String projectId) throws DatabaseException, AccessDeniedException, EntityNotFoundException {
		if(!authzService.allowAccess(FmsProject.class, projectId, AccessType.ADMIN)) {
			throw new NotAuthorizedException(FmsProject.class);
		}
		FmsProject out = adminDataStore.getObject(FmsProject.class, projectId); 
		if(out==null) {
			throw new EntityNotFoundException(FmsProject.class, projectId);
		}
		return out;
	}

	@Override
	public FmsProject saveProject(FmsProject project) throws AccessDeniedException, DatabaseException, VersioningException {
		if(project.getId()!=null && !authzService.allowAccess(FmsProject.class, project.getId(), AccessType.ADMIN)) {
			throw new NotAuthorizedException(FmsProject.class,project.getId());
		} else if (project.getId()==null && !authzService.allowAccess(FmsProject.class, AccessType.ADMIN)) {
			throw new NotAuthorizedException(FmsProject.class);
		}
		return adminDataStore.saveObject(project);
	}

	@Override
	public void deleteProject(String projectId) throws AccessDeniedException, DatabaseException {
		if(!authzService.allowAccess(FmsProject.class, projectId, AccessType.ADMIN)) {
			throw new NotAuthorizedException(FmsProject.class,projectId);
		}
		adminDataStore.deleteObject(FmsProject.class, projectId);
	}
	
	@Override
	public List<FmsUpdateResult> updateAll() throws AccessDeniedException {
		
		if(!authzService.allowAccess(FmsProjectSettings.class, AccessType.ADMIN)) {
			throw new AccessDeniedException();
		}
		
		
		List<FmsUpdateResult> out = new ArrayList<FmsUpdateResult>();
		
		FmsUser u = FmsSessionHolder.getCurrentUser();
		FmsSession old = FmsSessionHolder.get(); 
		if(!u.isAdmin()) {
			old = FmsSessionHolder.get();
			FmsUser user = new FmsAdminUser();
			user.setName("[SYSTEM] ("+(u!=null?u.getName():"anon")+")");
			user.setDisplayName("System User ("+(u!=null?u.getDisplayName():"anon")+")");
			FmsSession session = new FmsSession();
			session.setUser(user);
			FmsSessionHolder.set(session);
		}

		try {

			for(GenericService<?> s : new GenericService<?>[] {
					mediaClipService, eventService, movieService, mediaClipTaskService, 
					playbackDeviceService, keyRequestService, keyService, certificateService}) {
				
				int up = 0;
				int err = 0;
				int total = 0;
				long t = 0;
				long m = -1;
				List<String> errors = new ArrayList<String>();
				try {
					for(FmsObject o : s.list(BasicQuery.createQuery())) {
						long mi = System.currentTimeMillis();
						log.info(" ***** UPDATING: "+o.getClass()+" / "+o.getId());
						total++;
						try {
							
							FmsObject o2 = dataStore.saveObject(o, true, false);
							if(o2.getVersion() != o.getVersion()) {up++;}
						} catch (Exception e) {
							err++;
							errors.add("Error updating data: "+s.getClass()+" / "+o.getId()+" "+StringUtil.getStackTrace(e));
						}
						
						mi = System.currentTimeMillis()-mi;
						
						if(m<mi) {
							m = mi;
						}
						t = t+mi;
					}
					
				} catch (Exception e) {
					errors.add("Error updating ANY data: "+s.getClass()+"  "+StringUtil.getStackTrace(e));
				}
				FmsUpdateResult r = new FmsUpdateResult(s.getClass(),total,up,err,errors);
				r.setMaxTime(m);
				r.setTotalTime(t);
				out.add(r);
				
			};
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			FmsSessionHolder.set(old);
		}
		return out;
	}
		
}
