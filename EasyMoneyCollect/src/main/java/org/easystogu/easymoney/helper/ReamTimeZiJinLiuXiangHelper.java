package org.easystogu.easymoney.helper;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.easymoney.common.RealTimeZiJinLiuVO;
import org.easystogu.utils.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class ReamTimeZiJinLiuXiangHelper {
	private static final String baseUrl = "http://data.eastmoney.com/zjlx/";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();

	// stockList is like: sh000001,sh601318
	// has prefix
	public List<RealTimeZiJinLiuVO> fetchDataFromWeb(List<String> stockIds) {
		List<RealTimeZiJinLiuVO> list = new ArrayList<RealTimeZiJinLiuVO>();
		for (String stockId : stockIds) {
			RealTimeZiJinLiuVO vo = this.fetchDataFromWeb(stockId);
			if (vo.isValidated()) {
				list.add(vo);
			}
		}
		return list;
	}

	public RealTimeZiJinLiuVO fetchDataFromWeb(String stockId) {
		RealTimeZiJinLiuVO vo = new RealTimeZiJinLiuVO(stockId);
		StringBuffer urlStr = new StringBuffer(baseUrl + stockId + ".html");
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
			return vo;
		}

		String[] lines = contents.split("\n");
		for (int index = 0; index < lines.length; index++) {
			String line = lines[index];
			if (Strings.isNotEmpty(line)) {
				if (line.contains("今日主力净流入")) {
					System.out.println(lines[index + 1]);
				} else if (line.contains("主力净比")) {
					System.out.println(lines[index]);
				} else if (line.contains("今日超大单净流入")) {
					System.out.println(lines[index + 1]);
				} else if (line.contains("超大单净比")) {
					System.out.println(lines[index]);
				}
			}
		}

		// set date field
		System.out.println(vo);
		return vo;
	}

	public static void main(String[] args) {
		ReamTimeZiJinLiuXiangHelper ins = new ReamTimeZiJinLiuXiangHelper();
		ins.fetchDataFromWeb("300186");
	}
}
