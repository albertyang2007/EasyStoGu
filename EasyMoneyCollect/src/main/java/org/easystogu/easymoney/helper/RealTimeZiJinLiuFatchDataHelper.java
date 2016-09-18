package org.easystogu.easymoney.helper;

import java.io.ByteArrayInputStream;

import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.network.RestTemplateHelper;
import org.easystogu.utils.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//real time zijinliu
public class RealTimeZiJinLiuFatchDataHelper {
	private static final String baseUrl = "http://data.eastmoney.com/zjlx/";
	private static RealTimeZiJinLiuFatchDataHelper instance = null;

	private RealTimeZiJinLiuFatchDataHelper() {

	}

	public static RealTimeZiJinLiuFatchDataHelper getInstance() {
		if (instance == null) {
			instance = new RealTimeZiJinLiuFatchDataHelper();
		}
		return instance;
	}

	public ZiJinLiuVO fetchDataFromWeb(String stockId) {
		ZiJinLiuVO vo = new ZiJinLiuVO(stockId);
		StringBuffer urlStr = new StringBuffer(baseUrl + stockId + ".html");

		try {
			String contents = new RestTemplateHelper().fetchDataFromWeb(urlStr.toString());

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

			//System.out.println(vo.toNetInString());
			//System.out.println(vo.toNetPerString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	public static void main(String[] args) {
		RealTimeZiJinLiuFatchDataHelper runner = new RealTimeZiJinLiuFatchDataHelper();
		runner.fetchDataFromWeb("000049");
	}
}
