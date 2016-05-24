/**
 * Configuration Reader utility.
 */
package com.learning.qos.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * The class is used to access application configuration from config.properties file
 */
public final class QoEConfiguration {

	private static Logger logger = Logger.getLogger(QoEConfiguration.class);
	// Configuration is locked while reloading so only lock needed is
	// while reading config properties from multiple threads
	private Properties props  = new Properties();
	
	private QoEConfiguration() {
	    try {
	        props.load(new FileInputStream(new File("config.properties")));
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	}

	
	private static class QoEConfigurationInstance {
	    private static final QoEConfiguration config = new QoEConfiguration();
	}

	public static QoEConfiguration getInstance() {
	    return QoEConfigurationInstance.config;
	}
	/**
	 * This method is used to fetch the config property from the configuration
	 * file. If the property is null, then null is returned.
	 * 
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public String getConfigProperty(final String property) {
		return props.getProperty(property);
	}

	
}
