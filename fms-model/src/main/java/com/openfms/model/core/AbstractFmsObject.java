package com.openfms.model.core;

import java.util.Date;
import java.util.Locale;

import javax.persistence.Id;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openfms.model.annotations.Searchable;
import com.openfms.utils.common.text.MessageLoader;

@SuppressWarnings("rawtypes")
public abstract class AbstractFmsObject implements FmsObject, Comparable {
	
	private static final long serialVersionUID = -7686702573935364493L;
	
	private static MessageSource messageSource = MessageLoader.getMessageSource(AbstractFmsObject.class);

	@Id
	private String id;
	private String name;
	
	private long version;

	private Date updated;
	private Date created;
	
	public AbstractFmsObject(String id) {
		super();
		this.id = id;
	}

	public AbstractFmsObject() {
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "updated", "created", "version");
	}
	
	public int compareTo(Object other) {
		return CompareToBuilder.reflectionCompare(this,other, "updated", "created", "version");
	}
	
	public String getObjectTypeDisplayName() {
		String out = getObjectTypeDisplayName(LocaleContextHolder.getLocale());
		if(out == null) {
			out = getClass().getCanonicalName();
			out = out.substring(out.lastIndexOf(".")+1);
		}
		return out;
	}
	
	public String getObjectTypeDisplayName(Locale locale) {
		if(locale==null) {
			return null;
		}
		try {
			MessageSourceResolvable msr = new DefaultMessageSourceResolvable(getClass().getCanonicalName());
			return getMessageSource().getMessage(msr,locale);
		} catch (Exception e) {
			return null;
		}
	}

	public String getObjectTypeName() {
		return getClass().getCanonicalName();
	}
	
	public void setObjectTypeName(String t) {
	}

	public void setObjectTypeDisplayName(String t) {
	}
	
	public void setTypeName(String typeName) {
	}

	@Override
	public Date getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public long getVersion() {
		return version;
	}

	@Override
	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	@Searchable
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getSearchable() {
		return getName();
	}

	@Override
	public void setSearchable(String searchable) {
	}
	
	protected String concat(String... strings) {
		StringBuffer sb = new StringBuffer();
		for(String s : strings) {
			if(s!=null) {
				sb.append(s);
				sb.append(' ');
			}
		}
		return sb.toString();
	}
	

}
