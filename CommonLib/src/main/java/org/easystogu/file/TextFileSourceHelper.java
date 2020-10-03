package org.easystogu.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component
public class TextFileSourceHelper {
    private static Logger logger = LogHelper.getLogger(TextFileSourceHelper.class);
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    public String loadContent(String fileName) {
        try {
            String resourcesFilePath = "classpath:/" + fileName;
            System.out.println("Loading file " + resourcesFilePath);

            Resource resource = resourceLoader.getResource(resourcesFilePath);
            return this.loadContent(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loadContent(Resource resource) {
        StringBuffer lines = new StringBuffer();

        InputStream fis = null;
        try {
            fis = new FileInputStream(resource.getFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                if (Strings.isNotEmpty(line)) {
                    lines.append(line + "\n");
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

        return lines.toString();
    }

    public List<String> listResourceFiles(String pattern) {
        List<String> files = new ArrayList<String>();
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources(pattern);
            for (Resource res : resources) {
                files.add(res.getFilename());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return files;
    }
}
