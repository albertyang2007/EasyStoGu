package org.albertyang2007.easystogu.common;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ResourceLoaderHelper {
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    public static File loadAsFile(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return resource.getFile();
    }
}
