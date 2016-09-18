package org.easystogu.easymoney.helper;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.table.ZhuLiJingLiuRuVO;
import org.easystogu.network.HtmlUnitHelper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

//今日排行，5日排行，10日排行
public class DailyZhuLiJingLiuRuFatchDataHelper {

    // one page contains 50 stockIds and total 2914/50=59 pages
    public static final int totalPages = 59;
    private final String baseUrl = "http://data.eastmoney.com/zjlx/list.html";
    public String currentDate = "";

    public List<ZhuLiJingLiuRuVO> getAllStockIdsZiJinLiu(int toPage) {

        List<ZhuLiJingLiuRuVO> list = new ArrayList<ZhuLiJingLiuRuVO>();
        WebClient webClient = HtmlUnitHelper.getWebClient();
        try {
            HtmlPage htmlpage = webClient.getPage(baseUrl);
            // System.out.println(htmlpage.asXml());

            // fetch current date
            String dateTime = htmlpage.getElementById("datatime").asText().trim();
            // System.out.println("dateTime="+dateTime);
            currentDate = dateTime.substring(1, dateTime.length() - 1);

            // first page content
            HtmlTable tabContent = (HtmlTable) htmlpage.getElementById("dt_1");
            // System.out.println("tabContent=\n" + tabContent.asText());
            List<ZhuLiJingLiuRuVO> rtn = this.parseOnePageStockIdsZhuLiJingLiuRu(tabContent.asText());
            System.out.println("Process 1 day ZhuLiJingLiuRu Page 1 end with vo size: " + rtn.size());
            if (rtn.size() <= 0)
                return list;
            list.addAll(rtn);

            // now go thru the second page till end page
            if (toPage >= 2) {
                for (int page = 2; page <= toPage; page++) {
                    System.out.println("Process 1 day ZhuLiJingLiuRu Page " + page);
                    HtmlDivision div = (HtmlDivision) htmlpage.getElementById("PageCont");
                    HtmlTextInput input = div.getElementById("gopage");
                    input.setValueAttribute("" + page);
                    List<?> links = div.getByXPath("a");
                    HtmlAnchor anchor = (HtmlAnchor) links.get(links.size() - 1);
                    htmlpage = (HtmlPage) anchor.click();
                    webClient.waitForBackgroundJavaScript(1000 * 10L);
                    tabContent = (HtmlTable) htmlpage.getElementById("dt_1");
                    rtn = this.parseOnePageStockIdsZhuLiJingLiuRu(tabContent.asText());
                    System.out
                            .println("Process 1 day ZhuLiJingLiuRu Page " + page + " end with vo size: " + rtn.size());
                    if (rtn.size() <= 0)
                        return list;
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

    private List<ZhuLiJingLiuRuVO> parseOnePageStockIdsZhuLiJingLiuRu(String content) {
        List<ZhuLiJingLiuRuVO> list = new ArrayList<ZhuLiJingLiuRuVO>();
        String[] lines = content.split("\n");
        for (int i = 2; i < lines.length; i++) {
            // first two lines are table header
            if (lines[i].trim().length() > 1) {
                String line = lines[i].replaceAll("\\s{1,}", " ");
                String[] data = line.trim().split(" ");
                // System.out.println("len=" + data.length);
                if (data.length == 16) {
                    try {
                        ZhuLiJingLiuRuVO vo = new ZhuLiJingLiuRuVO();
                        // 8 600053 中江地产 大单详情 股吧 41.25 38.61% 8 10.00% 31.86% 8
                        // 61.07% 36.55% 7 159.43% 房地产
                        vo.rate = Integer.parseInt(data[0]);
                        vo.stockId = data[1];
                        vo.name = data[2].trim();
                        if (data[5].trim().contains("-"))
                            break;//do not collect invalidate data
                        vo.price = Double.parseDouble(data[5]);
                        vo.majorNetPer = convertNetPer2Double(data[6]);
                        if (vo.majorNetPer <= 0)
                            break;//do not collect zhuLiJingLiuChu stock
                        vo.incPer = data[8];

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
        DailyZhuLiJingLiuRuFatchDataHelper helper = new DailyZhuLiJingLiuRuFatchDataHelper();
        List<ZhuLiJingLiuRuVO> list = helper.getAllStockIdsZiJinLiu(2);
        for (ZhuLiJingLiuRuVO vo : list) {
            System.out.println(vo.stockId + " " + vo.name + " " + vo.price + " " + vo.toNetInString());
        }
    }
}
