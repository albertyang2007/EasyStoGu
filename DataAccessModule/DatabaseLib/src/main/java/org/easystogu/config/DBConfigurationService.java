package org.easystogu.config;

import org.easystogu.db.access.table.WSFConfigTableHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DBConfigurationService implements ConfigurationService {
	@Autowired
	private WSFConfigTableHelper wsfConfig;

	public Object getObject(String key) {
		return wsfConfig.getValue(key);
	}

	public String getValue(String key) {
		// TODO Auto-generated method stub
		return wsfConfig.getValue(key);
	}

	public String getValue(String key, String defaultValue) {
		// TODO Auto-generated method stub
		String v = wsfConfig.getValue(key);
		if (Strings.isNotEmpty(v))
			return v;
		return defaultValue;
	}

	public boolean getBoolean(String key) {
		// TODO Auto-generated method stub
		return Boolean.parseBoolean(wsfConfig.getValue(key));
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		// TODO Auto-generated method stub
		String v = wsfConfig.getValue(key);
		if (v != null) {
			return Boolean.parseBoolean(v);
		}
		return defaultValue;
	}

	public double getDouble(String key) {
		// TODO Auto-generated method stub
		return Double.parseDouble(wsfConfig.getValue(key));
	}

	public double getDouble(String key, double defaultValue) {
		// TODO Auto-generated method stub
		String v = wsfConfig.getValue(key);
		if (v != null) {
			return Double.parseDouble(v);
		}
		return defaultValue;
	}

	public int getInt(String key) {
		// TODO Auto-generated method stub
		return Integer.parseInt(wsfConfig.getValue(key));
	}

	public int getInt(String key, int defaultValue) {
		// TODO Auto-generated method stub
		String v = wsfConfig.getValue(key);
		if (v != null) {
			return Integer.parseInt(v);
		}
		return defaultValue;
	}

	public String getString(String key) {
		// TODO Auto-generated method stub
		return this.getValue(key);
	}

	public String getString(String key, String defaultValue) {
		// TODO Auto-generated method stub
		return this.getValue(key, defaultValue);
	}
}
