package org.easystogu.config;

public interface ConfigurationService {
	public String getValue(String key);

	public String getValue(String key, String defaultValue);

	public boolean getBoolean(String key);

	public boolean getBoolean(String key, boolean defaultValue);

	public double getDouble(String key);

	public double getDouble(String key, double defaultValue);

	public int getInt(String key);

	public int getInt(String key, int defaultValue);

	public String getString(String key);

	public String getString(String key, String defaultValue);
	
	public Object getObject(String key); 
}
