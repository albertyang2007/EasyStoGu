package org.easystogu.sina.helper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class SinaDataDownloadHelper {
	private static final String baseUrl = "http://hq.sinajs.cn/list=";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();

	// stockList is like: sh000001,sh601318
	// has prefix
	public List<RealTimePriceVO> fetchDataFromWeb(String stockList) {
		String[] stockIds = stockList.split(",");
		return this.fetchDataFromWeb(Arrays.asList(stockIds));
	}

	public List<RealTimePriceVO> fetchDataFromWeb(List<String> stockIds) {
		List<RealTimePriceVO> list = new ArrayList<RealTimePriceVO>();
		StringBuffer urlStr = new StringBuffer(baseUrl);
		for (String stockId : stockIds) {
			urlStr.append(stockId + ",");
		}

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(10000);
		requestFactory.setReadTimeout(10000);

		if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(configure.getString(Constants.httpProxyServer),
					configure.getInt(Constants.httpProxyPort)));
			requestFactory.setProxy(proxy);
		}

		RestTemplate restTemplate = new RestTemplate(requestFactory);

		String contents = restTemplate.getForObject(urlStr.toString(), String.class);

		if (Strings.isEmpty(contents)) {
			System.out.println("Contents is empty");
			return list;
		}

		String[] content = contents.trim().split("\n");
		for (int index = 0; index < content.length; index++) {
			String[] items = content[index].trim().split("\"");
			if (items.length <= 1) {
				continue;
			}
			// System.out.println(items[1]);
			// stockId has prefix, so remove it (sh, sz)
			String realStockId = stockIds.get(index).substring(2);
			RealTimePriceVO vo = new RealTimePriceVO(realStockId, items[1]);
			// System.out.println(vo);
			if (vo.isValidated())
				list.add(vo);
		}
		return list;
	}

	public static void main(String[] args) {

	}

}
