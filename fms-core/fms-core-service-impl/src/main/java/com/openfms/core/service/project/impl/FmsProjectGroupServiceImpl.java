package com.openfms.core.service.project.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import com.openfms.core.service.project.FmsProjectGroupService;
import com.openfms.model.annotations.FmsAccessControlled;
import com.openfms.model.annotations.FmsAccessControlled.GROUP;
import com.openfms.model.core.FmsObject;
import com.openfms.model.core.auth.AccessType;
import com.openfms.model.core.auth.FmsGroup;
import com.openfms.model.exceptions.EntityNotFoundException;

@Service
public class FmsProjectGroupServiceImpl extends GenericProjectServiceImpl<FmsGroup> implements FmsProjectGroupService {

	private static Log log = LogFactory.getLog(FmsProjectGroupServiceImpl.class);
	
	@Override
	public List<String> getControlledEntities() {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);		
	    provider.addIncludeFilter(new AnnotationTypeFilter(FmsAccessControlled.class));
	    provider.addIncludeFilter(new AssignableTypeFilter(FmsObject.class));
	    Set<BeanDefinition> beans = provider.findCandidateComponents("com");
	    List<String> out = new ArrayList<String>();
	    for (BeanDefinition bd : beans) {
	    	out.add(bd.getBeanClassName());
	    }
	    return out;
	}
	
	@Override
	public List<String> getControlledEntityGroups(String searchTerm) {
		List<String> out = new ArrayList<String>();
		for(GROUP g : FmsAccessControlled.GROUP.values()) {
			if(searchTerm == null || g.name().toLowerCase().contains(searchTerm.toLowerCase())) {
				out.add(g.name());
			}
		}
		return out;
	}
	
	@Override
	public List<String> getAccessTypes(String searchTerm) {
		List<String> out = new ArrayList<String>();
		for(AccessType t : AccessType.values()) {
			if(searchTerm == null || t.name().toLowerCase().contains(searchTerm.toLowerCase())) {
				out.add(t.name());
			}
		}
		return out;
	}

	@Override
	public String getAccessType(String id) throws EntityNotFoundException {
		for(AccessType t : AccessType.values()) {
			if(t.name().toLowerCase().equals(id.toLowerCase())) {
				return t.name();
			}
		}
		throw new EntityNotFoundException(String.class,id);
	}
	
	@Override
	public String getEntityGroup(String id) throws EntityNotFoundException {
		for(FmsAccessControlled.GROUP t : FmsAccessControlled.GROUP.values()) {
			if(t.name().toLowerCase().equals(id.toLowerCase())) {
				return t.name();
			}
		}
		throw new EntityNotFoundException(String.class,id);
	}
	
}
