package org.easystogu.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class TextFileSourceHelper {
	private static Logger logger = LogHelper.getLogger(TextFileSourceHelper.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private static TextFileSourceHelper instance = null;

	public static TextFileSourceHelper getInstance() {
		if (instance == null) {
			instance = new TextFileSourceHelper();
		}
		return instance;
	}

	private TextFileSourceHelper() {

	}

	public String[] loadContent(String fileName) {
		List<String> lines = new ArrayList<String>();
		String resourcesFilePath = "classpath:/" + fileName;

		System.out.println("Loading file " + resourcesFilePath);

		InputStream fis = null;
		try {
			Resource resource = resourceLoader.getResource(resourcesFilePath);
			fis = new FileInputStream(resource.getFile());
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "gb2312"));
			String line = reader.readLine();
			while (line != null) {
				if (Strings.isNotEmpty(line)) {
					lines.add(line);
				}
				line = reader.readLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					// System.out.println("Close resource file.");
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return lines.toArray(new String[] {});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
