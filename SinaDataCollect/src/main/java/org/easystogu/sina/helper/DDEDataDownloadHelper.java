package org.easystogu.sina.helper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class DDEDataDownloadHelper {
	private static final String baseUrl = "http://ddx.gubit.cn/ddelist.html?code=";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();

	public RealTimePriceVO fetchDataFromWeb(String stockId) {

		StringBuffer urlStr = new StringBuffer(baseUrl + stockId);

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
			return null;
		}

		System.out.println(contents);

		String[] content = contents.trim().split("\n");
		for (int index = 0; index < content.length; index++) {

		}

		return null;
	}

	public List<RealTimePriceVO> fetchDataFromWeb(List<String> stockIds) {
		List<RealTimePriceVO> list = new ArrayList<RealTimePriceVO>();
		for (String stockId : stockIds) {
			list.add(this.fetchDataFromWeb(stockId));
		}
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DDEDataDownloadHelper ins = new DDEDataDownloadHelper();
		ins.fetchDataFromWeb("600175");
	}

}
