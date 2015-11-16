package org.easystogu.easymoney.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.network.HtmlUnitHelper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;

//compare to DailyZiJinLiuFatchDataHelper, this helper can fatch all stockIds data at one time
//东方财富通网站全部A股资金流排名
//有当天数据，3天数据，5天数据,10天数据
//总共59页
public class DailyZiJinLiuFatchDataHelper {
	// one page contains 50 stockIds and total 2914/50=59 pages
	public static final int totalPages = 59;
	private final String baseUrl = "http://data.eastmoney.com/zjlx/detail.html";
	public String currentDate = "";

	public List<ZiJinLiuVO> getAllStockIdsZiJinLiu(int toPage) {
		return this.get1DayAllStockIdsZiJinLiu(toPage);
	}

	public List<ZiJinLiuVO> get3DayAllStockIdsZiJinLiu(int toPage) {
		return this.getNDayAllStockIdsZiJinLiu("DCFFITA3", toPage);
	}

	public List<ZiJinLiuVO> get5DayAllStockIdsZiJinLiu(int toPage) {
		return this.getNDayAllStockIdsZiJinLiu("DCFFITA5", toPage);
	}

	public List<ZiJinLiuVO> get10DayAllStockIdsZiJinLiu(int toPage) {
		return this.getNDayAllStockIdsZiJinLiu("DCFFITA10", toPage);
	}

	private List<ZiJinLiuVO> get1DayAllStockIdsZiJinLiu(int toPage) {
		List<ZiJinLiuVO> list = new ArrayList<ZiJinLiuVO>();
		WebClient webClient = HtmlUnitHelper.getWebClient();
		try {
			HtmlPage htmlpage = webClient.getPage(baseUrl);

			// fetch current date
			String dateTime = htmlpage.getElementById("datatime").asText().trim();
			currentDate = dateTime.substring(1, dateTime.length() - 1);

			// first page content
			HtmlTable tabContent = (HtmlTable) htmlpage.getElementById("dt_1");
			List<ZiJinLiuVO> rtn = this.parseOnePageStockIdsZiJinLiu(tabContent.asText());
			System.out.println("Process 1 day ZiJinLiu Page 1 end with vo size: " + rtn.size());
			list.addAll(rtn);

			// now go thru the second page till end page
			if (toPage >= 2) {
				for (int page = 2; page <= toPage; page++) {
					System.out.println("Process 1 day ZiJinLiu Page " + page);
					HtmlDivision div = (HtmlDivision) htmlpage.getElementById("PageCont");
					HtmlTextInput input = div.getElementById("gopage");
					input.setValueAttribute("" + page);
					List<?> links = div.getByXPath("a");
					HtmlAnchor anchor = (HtmlAnchor) links.get(links.size() - 1);
					htmlpage = (HtmlPage) anchor.click();
					webClient.waitForBackgroundJavaScript(1000 * 10L);
					tabContent = (HtmlTable) htmlpage.getElementById("dt_1");
					rtn = this.parseOnePageStockIdsZiJinLiu(tabContent.asText());
					System.out.println("Process 1 day ZiJinLiu Page " + page + " end with vo size: " + rtn.size());
					list.addAll(rtn);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			webClient.closeAllWindows();
		}

		return list;
	}

	private List<ZiJinLiuVO> getNDayAllStockIdsZiJinLiu(String DCFFITA, int toPage) {
		List<ZiJinLiuVO> list = new ArrayList<ZiJinLiuVO>();
		WebClient webClient = HtmlUnitHelper.getWebClient();
		try {
			HtmlPage htmlpage = webClient.getPage(baseUrl);

			// fetch current date
			String dateTime = htmlpage.getElementById("datatime").asText().trim();
			currentDate = dateTime.substring(1, dateTime.length() - 1);

			// find 3日排行
			HtmlElement element3Day = null;
			HtmlUnorderedList statType = (HtmlUnorderedList) htmlpage.getElementById("stat_type");
			Iterator<HtmlElement> eleDays = statType.getChildElements().iterator();
			while (eleDays.hasNext()) {
				HtmlListItem eleDay = (HtmlListItem) eleDays.next();
				if (eleDay.asXml().contains(DCFFITA)) {
					element3Day = eleDay;
					break;
				}
			}

			htmlpage = element3Day.click();
			webClient.waitForBackgroundJavaScript(1000 * 10L);

			// first page content
			HtmlTable tabContent = (HtmlTable) htmlpage.getElementById("dt_1");
			List<ZiJinLiuVO> rtn = this.parseOnePageStockIdsZiJinLiu(tabContent.asText());
			System.out.println("Process " + DCFFITA + " day ZiJinLiu Page 1 end with vo size: " + rtn.size());
			list.addAll(rtn);

			// now go thru the second page till end page
			if (toPage >= 2) {
				for (int page = 2; page <= toPage; page++) {
					System.out.println("Process " + DCFFITA + " day ZiJinLiu Page " + page);
					HtmlDivision div = (HtmlDivision) htmlpage.getElementById("PageCont");
					HtmlTextInput input = div.getElementById("gopage");
					input.setValueAttribute("" + page);
					List<?> links = div.getByXPath("a");
					HtmlAnchor anchor = (HtmlAnchor) links.get(links.size() - 1);
					htmlpage = (HtmlPage) anchor.click();
					webClient.waitForBackgroundJavaScript(1000 * 10L);
					tabContent = (HtmlTable) htmlpage.getElementById("dt_1");
					rtn = this.parseOnePageStockIdsZiJinLiu(tabContent.asText());
					System.out.println("Process " + DCFFITA + " day ZiJinLiu Page " + page + " end with vo size: "
							+ rtn.size());
					list.addAll(rtn);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			webClient.closeAllWindows();
		}

		return list;
	}

	private List<ZiJinLiuVO> parseOnePageStockIdsZiJinLiu(String content) {
		List<ZiJinLiuVO> list = new ArrayList<ZiJinLiuVO>();
		String[] lines = content.split("\n");
		for (int i = 2; i < lines.length; i++) {
			// first two lines are table header
			if (lines[i].trim().length() > 1) {
				String line = lines[i].replaceAll("\\s{1,}", " ");
				String[] data = line.trim().split(" ");
				if (data.length == 18) {
					try {
						ZiJinLiuVO vo = new ZiJinLiuVO();
						// 19 601186 中国铁建 大单详情 股吧 研报 16.47 3.65% 1.56亿 8.25%
						// 2.99亿
						// 15.83% -1.43亿 -7.58% -1.03亿 -5.44% -5313万 -2.81%
						vo.rate = Integer.parseInt(data[0]);
						vo.stockId = data[1];
						vo.name = data[2].trim();

						vo.majorNetIn = convertNetIn2Double(data[8]);
						vo.majorNetPer = convertNetPer2Double(data[9]);

						vo.biggestNetIn = convertNetIn2Double(data[10]);
						vo.biggestNetPer = convertNetPer2Double(data[11]);

						vo.bigNetIn = convertNetIn2Double(data[12]);
						vo.bigNetPer = convertNetPer2Double(data[13]);

						vo.midNetIn = convertNetIn2Double(data[14]);
						vo.midNetPer = convertNetPer2Double(data[15]);

						vo.smallNetIn = convertNetIn2Double(data[16]);
						vo.smallNetPer = convertNetPer2Double(data[17]);

						vo.date = this.currentDate;

						// System.out.println(vo.toNetInString());
						// System.out.println(vo.toNetPerString());
						list.add(vo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}

	private double convertNetPer2Double(String item) {
		if (item == null || "".equals(item) || "-".equals(item)) {
			return 0;
		}

		if (item.lastIndexOf('%') == item.length() - 1) {
			return Double.parseDouble(item.substring(0, item.length() - 1));
		}
		return 0;
	}

	private double convertNetIn2Double(String item) {
		if (item == null || "".equals(item) || "-".equals(item) || "0".equals(item)) {
			return 0;
		}
		// item is like: 1.2亿 or 5600万, 返回单位为万
		if (item.contains("亿")) {
			return Double.parseDouble(item.substring(0, item.length() - 1)) * 10000;
		} else if (item.contains("万")) {
			return Double.parseDouble(item.substring(0, item.length() - 1));
		} else {
			return Double.parseDouble(item.substring(0, item.length() - 1)) / 10000;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailyZiJinLiuFatchDataHelper helper = new DailyZiJinLiuFatchDataHelper();
		List<ZiJinLiuVO> list = helper.get5DayAllStockIdsZiJinLiu(2);
		for (ZiJinLiuVO vo : list) {
			System.out.println(vo.stockId + "=" + vo.name);
		}
	}

}
