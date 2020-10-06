package org.easystogu.portal.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

@Component
public class TrendModeLoader {
	private static Logger logger = LogHelper.getLogger(TrendModeLoader.class);
	@Autowired
	private TextFileSourceHelper fileSource;
	private Map<String, TrendModeVO> trendModeMap = new HashMap<String, TrendModeVO>();
	private static Map<String, Class> classMap = new HashMap<String, Class>();
	static {
		classMap.put("prices", SimplePriceVO.class);
	}

	@PostConstruct
	public void startUp() {
		try {
			List<Resource> resources = fileSource.listResourceFile("classpath:/TrendMode/*.json");
			logger.debug("Initialization loadTrendModeFromResource, len=" + resources.size());
			for (Resource resource : resources) {
				String name = resource.getFilename();
				String content = fileSource.loadContent(resource);
				if (Strings.isEmpty(content)) {
					System.err.println("fileName " + name + " content is null");
					continue;
				}
				JSONObject jsonObject = JSONObject.fromObject(content);
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

	public TrendModeVO loadTrendMode(String name) {
		TrendModeVO tmo = this.trendModeMap.get(name);
		if (tmo == null) {
			logger.debug("loadTrendMode is null for " + name);
		}
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

	// display error when have Chinese
	public List<String> getAllDisplayNames() {
		List<String> names = new ArrayList<String>();
		Collection<TrendModeVO> list = this.trendModeMap.values();
		for (TrendModeVO tmo : list) {
			names.add(tmo.name);
		}
		return names;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrendModeLoader ins = new TrendModeLoader();
		ins.startUp();
		ins.loadTrendMode("test");
	}

}
