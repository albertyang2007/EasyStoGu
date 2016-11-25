package org.easystogu.config;

import org.easystogu.db.access.table.WSFConfigTableHelper;
import org.easystogu.utils.Strings;

public class DBConfigurationService implements ConfigurationService {
	private WSFConfigTableHelper wsfConfig = WSFConfigTableHelper.getInstance();
	private static DBConfigurationService instance = null;

	public static DBConfigurationService getInstance() {
		if (instance == null) {
			instance = new DBConfigurationService();
		}
		return instance;
	}

	private DBConfigurationService() {

	}

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConfigurationService ins = DBConfigurationService.getInstance();
		System.out.println(ins.getString("zone"));
	}
}
