package com.openfms.utils.common.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;

public class PropertyConfigurer extends PropertyResourceConfigurer implements BeanFactoryAware, BeanNameAware {

	private Log log = LogFactory.getLog(PropertyConfigurer.class);

	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

	private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

	private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

	private boolean ignoreUnresolvablePlaceholders = false;

	private String beanName;

	private BeanFactory beanFactory;
	
	private List<PropertyLookup> propertyLookups = new ArrayList<PropertyLookup>();

	public PropertyConfigurer() {
		log.warn(" ############################################################## ");
		log.warn(" ###   ");
		log.warn(" ###   PROPERTY CONFIGURER INSTANTIATED" );
		log.warn(" ###   ");
		log.warn(" ############################################################## ");
	}
	
	
	public void setPlaceholderPrefix(String placeholderPrefix) {
		this.placeholderPrefix = placeholderPrefix;
	}

	public void setPlaceholderSuffix(String placeholderSuffix) {
		this.placeholderSuffix = placeholderSuffix;
	}

	public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

		BeanDefinitionVisitor visitor = new PlaceholderResolvingBeanDefinitionVisitor(props);
		String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
		for (int i = 0; i < beanNames.length; i++) {
			if (!(beanNames[i].equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
				BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(beanNames[i]);
				try {
					visitor.visitBeanDefinition(bd);
				} catch (BeanDefinitionStoreException ex) {
					ex.printStackTrace();
					throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanNames[i], ex.getMessage());
				}
			}
		}
	}

	protected String parseStringValue(String strVal, Properties props, String originalPlaceholder) throws BeanDefinitionStoreException {

		StringBuffer buf = new StringBuffer(strVal);
		int startIndex = buf.indexOf(this.placeholderPrefix);
		while (startIndex != -1) {
			int endIndex = buf.indexOf(this.placeholderSuffix, startIndex + this.placeholderPrefix.length());
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
				String originalPlaceholderToUse = null;
				if (originalPlaceholder != null) {
					originalPlaceholderToUse = originalPlaceholder;
					if (placeholder.equals(originalPlaceholder)) {
						throw new BeanDefinitionStoreException("Circular placeholder reference '" + placeholder + "' in property definitions");
					}
				} else {
					originalPlaceholderToUse = placeholder;
				}

				String propVal = resolvePlaceholder(placeholder);
				if (propVal != null) {
					propVal = parseStringValue(propVal, props, originalPlaceholderToUse);
					buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
					if (log.isDebugEnabled()) {
						if(log.isDebugEnabled()){ log.debug("Resolved placeholder '" + placeholder + "' to value [" + propVal + "]"); }
					}
					startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
				} else if (this.ignoreUnresolvablePlaceholders) {
					// Proceed with unprocessed value.
					startIndex = buf.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
				} else {
					throw new BeanDefinitionStoreException("Could not resolve placeholder '" + placeholder + "'");
				}
			} else {
				startIndex = -1;
			}
		}

		return buf.toString();
	}

	protected String resolvePlaceholder(String placeholder) {
		for(PropertyLookup pl : getPropertyLookups()) {
			String s = pl.getProperty(placeholder);
			if(s!=null) {
				log.info("PROPERTIES: "+pl.getClass().getCanonicalName()+": has a value for "+placeholder+" ('"+s+"')");
				return s;
			}
			log.info("PROPERTIES: "+pl.getClass().getCanonicalName()+": has NO value for "+placeholder);
		}
		log.warn(" ############################################################## ");
		log.warn(" ###   ");
		log.warn(" ###   NO VALUE FOUND FOR: "+placeholder);
		log.warn(" ###   ");
		log.warn(" ############################################################## ");
		return null;
	}

	public List<PropertyLookup> getPropertyLookups() {
		return propertyLookups;
	}


	public void setPropertyLookups(List<PropertyLookup> propertyLookups) {
		this.propertyLookups = propertyLookups;
	}

	private class PlaceholderResolvingBeanDefinitionVisitor extends BeanDefinitionVisitor {

		private final Properties props;

		public PlaceholderResolvingBeanDefinitionVisitor(Properties props) {
			this.props = props;
		}

		@Override
		protected String resolveStringValue(String strVal) throws BeansException {
			return parseStringValue(strVal, this.props, null);
		}
	}

}
