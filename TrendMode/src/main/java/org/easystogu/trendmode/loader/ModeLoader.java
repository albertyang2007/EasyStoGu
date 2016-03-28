package org.easystogu.trendmode.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;

public class ModeLoader {
    private static Logger logger = LogHelper.getLogger(ModeLoader.class);
    private static ModeLoader instance = null;
    private TextFileSourceHelper fileSource = TextFileSourceHelper.getInstance();
    private Map<String, TrendModeVO> trendModeMap = new HashMap<String, TrendModeVO>();
    private static Map<String, Class> classMap = new HashMap<String, Class>();
    static {
        classMap.put("prices", SimplePriceVO.class);
    }

    private ModeLoader() {
        loadTrendModeFromResource();
    }

    public static ModeLoader getInstance() {
        if (instance == null) {
            instance = new ModeLoader();
        }
        return instance;
    }

    private void loadTrendModeFromResource() {
        try {
            List<String> names = fileSource.listResourceFiles("classpath:/*.json");
            //why names.size = 0 when invoke query/listnames from web??? Different classloader???
            System.out.println("ModeLoader loadTrendModeFromResource, len=" + names.size());
            for (String name : names) {
                JSONObject jsonObject = JSONObject.fromObject(fileSource.loadContent(name));
                TrendModeVO tmo = (TrendModeVO) JSONObject.toBean(jsonObject, TrendModeVO.class, classMap);
                trendModeMap.put(name.split("\\.")[0], tmo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TrendModeVO loadTrendMode(String name) {
        TrendModeVO tmo = this.trendModeMap.get(name);
        if (tmo == null) {
            System.out.println(name + " not found in Map, load it from resource.");
            String content = fileSource.loadContent(name + ".json");
            if (Strings.isNotEmpty(content)) {
                JSONObject jsonObject = JSONObject.fromObject(content);
                tmo = (TrendModeVO) JSONObject.toBean(jsonObject, TrendModeVO.class, classMap);
                trendModeMap.put(name, tmo);
            }
        }
        //TODO: it will return null, pls consider handle the exception, 404 return to web request???
        return tmo;
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<String>();
        String[] str = this.trendModeMap.keySet().toArray(new String[0]);
        for (String s : str) {
            names.add(s);
        }
        return names;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ModeLoader ins = ModeLoader.getInstance();
        System.out.println(ins.getAllNames());
    }
}
