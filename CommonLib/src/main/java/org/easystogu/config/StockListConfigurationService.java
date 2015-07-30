package org.easystogu.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class StockListConfigurationService {

	private static Logger logger = LogHelper
			.getLogger(StockListConfigurationService.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private Properties properties = null;
	private static StockListConfigurationService instance = null;
	private Map<String, String> stockIdNameMap = new HashMap<String, String>();

	public static StockListConfigurationService getInstance() {
		if (instance == null) {
			instance = new StockListConfigurationService();
		}
		return instance;
	}

	private StockListConfigurationService() {
		String[] resourcesPaths = new String[2];
		resourcesPaths[0] = "classpath:/sh_list.properties";
		resourcesPaths[1] = "classpath:/sz_list.properties";
		properties = loadProperties(resourcesPaths);
		loadProperties2Map(resourcesPaths);
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
			} catch (FileNotFoundException ex) {
				logger.info("Properties not found from path:{}, {} ", location,
						ex.getMessage());
			} catch (Exception ex) {
				logger.info("Could not load properties from path:{}, {} ",
						location, ex.getMessage());
				ex.printStackTrace();
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

	private void loadProperties2Map(String... resourcesPaths) {
		for (String location : resourcesPaths) {
			logger.debug("Loading properties file from path:{}", location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				InputStreamReader insr = new InputStreamReader(is, "gb2312");
				BufferedReader read = new BufferedReader(insr);
				String line = null;
				while ((line = read.readLine()) != null) {
					if (line.contains("=")) {
						String[] pairs = line.trim().split("=");
						stockIdNameMap.put(pairs[0], pairs[1]);
					}
				}
				read.close();
				insr.close();
			} catch (FileNotFoundException ex) {
				logger.info("Properties not found from path:{}, {} ", location,
						ex.getMessage());
			} catch (Exception ex) {
				logger.info("Could not load properties from path:{}, {} ",
						location, ex.getMessage());
				ex.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	private String getValue(String key) {
		return properties.getProperty(key);
	}

	public String getString(String key) {
		return getValue(key);
	}

	public String getStockName(String stockId) {
		return this.stockIdNameMap.get(stockId);
	}

	public List<String> getAllStockId() {
		Enumeration keys = properties.keys();
		List<String> stockIds = new ArrayList<String>();
		while (keys.hasMoreElements()) {
			stockIds.add((String) keys.nextElement());
		}
		return stockIds;
	}

	public List<String> getAllSZStockId() {
		Enumeration keys = properties.keys();
		List<String> stockIds = new ArrayList<String>();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("0") || key.startsWith("3")) {
				stockIds.add(key);
			}
		}
		return stockIds;
	}

	public List<String> getAllSZStockId(String prefix) {
		Enumeration keys = properties.keys();
		List<String> stockIds = new ArrayList<String>();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("0") || key.startsWith("3")) {
				stockIds.add(prefix + key);
			}
		}
		return stockIds;
	}
	
	public String getSZZSStockId(){
		return "sh000001";
	}

	public List<String> getAllSHStockId() {
		Enumeration keys = properties.keys();
		List<String> stockIds = new ArrayList<String>();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("6")) {
				stockIds.add(key);
			}
		}
		return stockIds;
	}

	public List<String> getAllSHStockId(String prefix) {
		Enumeration keys = properties.keys();
		List<String> stockIds = new ArrayList<String>();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith("6")) {
				stockIds.add(prefix + key);
			}
		}
		return stockIds;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService ins = StockListConfigurationService
				.getInstance();
		List<String> shList = ins.getAllSHStockId();
		for (int i = 0; i < shList.size(); i++) {
			System.out.println(shList.get(i));
		}
	}
}
