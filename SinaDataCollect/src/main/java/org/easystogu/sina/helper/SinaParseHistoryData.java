package org.easystogu.sina.helper;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SinaParseHistoryData {

	public void parseFromFile(String file) {
		try {
			File input = new File(file);
			Document doc = Jsoup.parse(input, "UTF-8");

			// Elements 是 Element 的集合类
			Elements element = doc.select("table id='FundHoldSharesTable'");

			// 从查找到table属性的Elements集合中获取标签 tr 或者tr[class$=alt] 表示
			// tr标签内class属性=alt
			// Elements titleName=element.select("tr[class$=alt]");
			Elements titleName = element.select("tr");
			for (Element name : titleName) {
				System.out.println(name.text());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SinaParseHistoryData runner = new SinaParseHistoryData();
		runner.parseFromFile("I:/Stock/EasyStoGuProject/SinaHistoryData/000404_2009_1.html");
	}

}
