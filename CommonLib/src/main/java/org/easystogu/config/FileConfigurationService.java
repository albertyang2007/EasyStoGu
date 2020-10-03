package org.easystogu.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class FileConfigurationService implements ConfigurationService {
	private static Logger logger = LogHelper.getLogger(FileConfigurationService.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private Properties properties = new Properties();

	@PostConstruct
	private void init() {
		String[] resourcesPaths = new String[2];
		resourcesPaths[0] = "classpath:/application.properties";
		if (Strings.isNotEmpty(System.getProperty("properties.file"))) {
			resourcesPaths[1] = System.getProperty("properties.file");
		} else {
			resourcesPaths[1] = "application.properties";
		}
		loadPropertiesFromResource(resourcesPaths[0]);
		loadPropertiesFromPath(resourcesPaths[1]);
	}

	private void loadPropertiesFromResource(String... resourcesPaths) {
		for (String location : resourcesPaths) {

			logger.debug("Loading properties file from resource:{}", location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				properties.load(is);
			} catch (IOException ex) {
				logger.info("Could not load properties from resource:{}, {} ", location, ex.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.error("Exception: ", e);
					}
				}
			}
		}
	}

	private void loadPropertiesFromPath(String... resourcesPaths) {
		for (String location : resourcesPaths) {

			logger.debug("Loading properties file from path:{}", location);

			InputStream is = null;
			try {
				is = new FileInputStream(new File(location));
				properties.load(is);
			} catch (IOException ex) {
				logger.info("Could not load properties from path:{}, {} ", location, ex.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.error("Exception: ", e);
					}
				}
			}
		}
	}

	public String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		return properties.getProperty(key);
	}

	public String getValue(String key, String defaultValue) {
		String v = properties.getProperty(key);
		if (v != null) {
			return v;
		}
		return defaultValue;
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
}
