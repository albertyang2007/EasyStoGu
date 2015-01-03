package org.easystogu.yahoo.common;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

//yahoo历史数据
//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
public class ResourceLoaderHelper {
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	public static File loadAsFile(String path) throws IOException {
		Resource resource = resourceLoader.getResource(path);
		return resource.getFile();
	}
}
