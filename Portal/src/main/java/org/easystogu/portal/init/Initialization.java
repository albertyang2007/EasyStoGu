package org.easystogu.portal.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.json.JSONObject;

import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.springframework.stereotype.Component;

@Component
public class Initialization {
    private TextFileSourceHelper fileSource = TextFileSourceHelper.getInstance();
    private Map<String, TrendModeVO> trendModeMap = new HashMap<String, TrendModeVO>();
    private static Map<String, Class> classMap = new HashMap<String, Class>();
    static {
        classMap.put("prices", SimplePriceVO.class);
    }
    
    @PostConstruct
    public void startUp() {
        try {
            List<String> names = fileSource.listResourceFiles("file:../TrendMode/src/main/resources/*.json");
            //why names.size = 0 when invoke query/listnames from web??? Different classloader???
            System.out.println("Initialization loadTrendModeFromResource, len=" + names.size());
            for (String name : names) {
                JSONObject jsonObject = JSONObject.fromObject(fileSource.loadContent(name));
                TrendModeVO tmo = (TrendModeVO) JSONObject.toBean(jsonObject, TrendModeVO.class, classMap);
                trendModeMap.put(name.split("\\.")[0], tmo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void shutDown() {

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
        Initialization ins = new Initialization();
        ins.startUp();
    }

}
