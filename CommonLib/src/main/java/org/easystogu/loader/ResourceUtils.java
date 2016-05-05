package org.easystogu.loader;

import java.io.InputStream;

public class ResourceUtils {
    public static InputStream getResourceFromClasspath(String resourcePath){
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = contextLoader.getResourceAsStream(resourcePath);

        if (inputStream != null) {
            return inputStream;
        }
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();
        return classLoader.getResourceAsStream(resourcePath);
    }
}
