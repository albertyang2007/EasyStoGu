package org.easystogu.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

//txt file to store all company base info
//export all data from easymoney software PC version in "分析"->"财务数据", select text file format
//the Table_CompanyBaseInfo.txt is saved in CommonLib\src\main\resources
public class CompanyBaseInfoTableHelper {
	private static Logger logger = LogHelper.getLogger(CompanyBaseInfoTableHelper.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private final HashMap<String, CompanyBaseInfoVO> companyBaseInfo;
	private static CompanyBaseInfoTableHelper instance = null;

	public static CompanyBaseInfoTableHelper getInstance() {
		if (instance == null) {
			instance = new CompanyBaseInfoTableHelper();
		}
		return instance;
	}

	private CompanyBaseInfoTableHelper() {
		String[] resourcesPaths = new String[2];
		resourcesPaths[0] = "classpath:/Table_CompanyBaseInfo.txt";
		if (Strings.isNotEmpty(System.getProperty("easystogu.companyBaseInfoFile"))) {
			resourcesPaths[1] = System.getProperty("easystogu.companyBaseInfoFile");
		} else {
			resourcesPaths[1] = "Table_CompanyBaseInfo.txt";
		}
		companyBaseInfo = loadDataFromFile(resourcesPaths);
	}

	private HashMap<String, CompanyBaseInfoVO> loadDataFromFile(String... resourcesPaths) {
		HashMap<String, CompanyBaseInfoVO> map = new HashMap<String, CompanyBaseInfoVO>();

		for (String location : resourcesPaths) {

			logger.debug("Loading CompanyBaseInfo file from path:{}", location);

			FileInputStream fis = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				fis = new FileInputStream(resource.getFile());
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

				String line = reader.readLine();
				while (line != null) {
					if (Strings.isNotEmpty(line)) {
						CompanyBaseInfoVO vo = new CompanyBaseInfoVO(line.trim());
					}
					line = reader.readLine();
				}

			} catch (IOException ex) {
				logger.info("Could not load data from path:{}, {} ", location, ex.getMessage());
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return map;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyBaseInfoTableHelper.getInstance();
	}
}
