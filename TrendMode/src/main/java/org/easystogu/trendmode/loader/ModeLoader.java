package org.easystogu.trendmode.loader;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.slf4j.Logger;

public class ModeLoader {
	private static Logger logger = LogHelper.getLogger(ModeLoader.class);
	private static ModeLoader instance = null;
	private TextFileSourceHelper fileSource = TextFileSourceHelper.getInstance();
	private Map<String, TrendModeVO> trendModeMap = new HashMap<String, TrendModeVO>();

	private ModeLoader() {

	}

	public static ModeLoader getInstance() {
		if (instance == null) {
			instance = new ModeLoader();
		}
		return instance;
	}

	public TrendModeVO loadTrendMode(String name) {
		try {
			Map<String, Class> classMap = new HashMap<String, Class>();  			 
			classMap.put("prices", SimplePriceVO.class);  			
			JSONObject jsonObject = JSONObject.fromObject(this.loadContent(name));
			return (TrendModeVO) JSONObject.toBean(jsonObject, TrendModeVO.class, classMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TrendModeVO();
	}

	private String loadContent(String name) {
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
		TrendModeVO tmo = ins.loadTrendMode("M_Tou");
		for (int i = 0; i < tmo.prices.size(); i++) {
			SimplePriceVO svo = tmo.prices.get(i);
			System.out.println(svo.close);
		}
	}
}
