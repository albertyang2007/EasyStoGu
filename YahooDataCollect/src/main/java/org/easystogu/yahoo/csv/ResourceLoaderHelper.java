package org.easystogu.yahoo.csv;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

//yahoo��ʷ���
//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
public class ResourceLoaderHelper {
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	// load resource to file, the path is under project src/main/resources
	public static File loadResourceAsFile(String file) throws IOException {
		Resource resource = resourceLoader.getResource(file);
		return resource.getFile();
	}

	// load file, the path is a common unix/windown path
	public static File loadAsFile(String fullPathFile) throws IOException {
		return new File(fullPathFile);
	}
}
