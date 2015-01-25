package org.easystogu.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class FileConfigurationService {
	private static Logger logger = LogHelper
			.getLogger(FileConfigurationService.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private final Properties properties;
	private static FileConfigurationService instance = null;

	public static FileConfigurationService getInstance() {
		if (instance == null) {
			instance = new FileConfigurationService();
		}
		return instance;
	}

	private FileConfigurationService() {
		String[] resourcesPaths = new String[2];
		resourcesPaths[0] = "classpath:/application.properties";
		if (Strings.isNotEmpty(System.getProperty("easystogu.config"))) {
			resourcesPaths[1] = System.getProperty("easystogu.config");
		} else {
			resourcesPaths[1] = "application.properties";
		}
		properties = loadProperties(resourcesPaths);
	}

	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {

			logger.debug("Loading properties file from path:{}", location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				logger.info("Could not load properties from path:{}, {} ",
						location, ex.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return props;
	}

	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		return properties.getProperty(key);
	}

	public boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new RuntimeException("Property " + key + " is not exist");
		}
		return Boolean.valueOf(value);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		if (value == null) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}

	public double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new RuntimeException("Property " + key + " is not exist");
		}
		return Double.valueOf(value);
	}

	public double getDouble(String key, double defaultValue) {
		String value = getValue(key);
		if (value == null) {
			return defaultValue;
		}
		return Double.valueOf(value);
	}

	public int getInt(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new RuntimeException("Property " + key + " is not exist");
		}
		return Integer.valueOf(value);
	}

	public int getInt(String key, int defaultValue) {
		String value = getValue(key);
		if (value == null) {
			return defaultValue;
		}
		return Integer.valueOf(value);
	}

	public Object getObject(String key) {
		return getString(key);
	}

	public String getString(String key) {
		return getValue(key);
	}

	public String getString(String key, String defaultValue) {
		String value = getValue(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
