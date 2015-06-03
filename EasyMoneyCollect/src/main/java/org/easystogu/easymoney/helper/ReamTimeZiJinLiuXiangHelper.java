package org.easystogu.easymoney.helper;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.utils.Strings;
import org.easystogu.vo.RealTimeZiJinLiuVO;
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

		try {
			RestTemplate restTemplate = new RestTemplate(requestFactory);

			String contents = restTemplate.getForObject(urlStr.toString(), String.class);

			if (Strings.isEmpty(contents)) {
				System.out.println("Contents of zijinliu for " + stockId + " is empty");
				return vo;
			}

			ByteArrayInputStream is = new ByteArrayInputStream(contents.getBytes("gb2312"));
			Document doc = Jsoup.parse(is, "gb2312", "");
			Elements elements = doc.select("div.flash-data-cont");
			for (Element element : elements) {
				Element jlr = element.getElementById("data_jlr");
				vo.majorNetIn = Double.parseDouble(jlr.text());

				Element jzb = element.getElementById("data_jzb");
				vo.majorNetPer = Double.parseDouble(jzb.text().substring(0, jzb.text().length() - 1));

				Element superjlr = element.getElementById("data_superjlr");
				vo.biggestNetIn = Double.parseDouble(superjlr.text());

				Element superjzb = element.getElementById("data_superjzb");
				vo.biggestNetPer = Double.parseDouble(superjzb.text().substring(0, superjzb.text().length() - 1));

				Element ddjlr = element.getElementById("data_ddjlr");
				vo.bigNetIn = Double.parseDouble(ddjlr.text());

				Element ddjzb = element.getElementById("data_ddjzb");
				vo.bigNetPer = Double.parseDouble(ddjzb.text().substring(0, ddjzb.text().length() - 1));

				Element zdjlr = element.getElementById("data_zdjlr");
				vo.midNetIn = Double.parseDouble(zdjlr.text());

				Element zdjzb = element.getElementById("data_zdjzb");
				vo.midNetPer = Double.parseDouble(zdjzb.text().substring(0, zdjzb.text().length() - 1));

				Element xdjlr = element.getElementById("data_xdjlr");
				vo.smallNetIn = Double.parseDouble(xdjlr.text());

				Element xdjzb = element.getElementById("data_xdjzb");
				vo.smallNetPer = Double.parseDouble(xdjzb.text().substring(0, xdjzb.text().length() - 1));

			}

			// System.out.println(vo.toNetInString());
			// System.out.println(vo.toNetPerString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	public static void main(String[] args) {
		ReamTimeZiJinLiuXiangHelper ins = new ReamTimeZiJinLiuXiangHelper();
		ins.fetchDataFromWeb("300186");
	}
}
