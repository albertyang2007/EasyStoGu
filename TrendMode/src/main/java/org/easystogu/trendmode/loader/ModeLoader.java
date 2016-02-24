package org.easystogu.trendmode.loader;

import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class ModeLoader {

    private static Logger logger = LogHelper.getLogger(ModeLoader.class);
    private static ModeLoader instance = null;
    protected TextFileSourceHelper fileSource = TextFileSourceHelper.getInstance();

    public static ModeLoader getInstance() {
        if (instance == null) {
            instance = new ModeLoader();
        }
        return instance;
    }

    public String loadContent(String name) {
        String[] lines = fileSource.loadContent(name + ".json");
        StringBuffer content = new StringBuffer();
        for (String line : lines) {
            content.append(line);
        }
        return content.toString();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ModeLoader ins = ModeLoader.getInstance();
    }
}
