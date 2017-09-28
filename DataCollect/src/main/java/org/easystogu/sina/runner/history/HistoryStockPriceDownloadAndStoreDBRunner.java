package org.easystogu.sina.runner.history;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.sina.common.SohuQuoteStockPriceVOWrap;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;

//history stock price from sohu, json format
//example: http://q.stock.sohu.com/hisHq?code=cn_002252&start=19990101&end=20170930&order=D&period=d&rt=json
public class HistoryStockPriceDownloadAndStoreDBRunner {
    // before 1997, there is no +-10%
    private String startDate = "1997-01-01";
    private String endDate = WeekdayUtil.currentDate();
    private static String baseUrl = "http://q.stock.sohu.com/hisHq?code=cn_stockId&start=startDate&end=endDate&order=D&period=d&rt=json";
    private static ConfigurationService configure = FileConfigurationService.getInstance();
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
    private CompanyInfoTableHelper companyInfoTable = CompanyInfoTableHelper.getInstance();
    private static Map<String, Class> classMap = new HashMap<String, Class>();
    static {
        classMap.put("hq", List.class);
    }

    public HistoryStockPriceDownloadAndStoreDBRunner() {
    }

    public HistoryStockPriceDownloadAndStoreDBRunner(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<StockPriceVO> fetchStockPriceFromWeb(List<String> stockIds) {
        List<StockPriceVO> list = new ArrayList<StockPriceVO>();
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 100 == 0)
                System.out.println("fetchStockPriceFromWeb: " + (index) + " of " + stockIds.size());
            list.addAll(this.fetchStockPriceFromWeb(stockId));
        }
        return list;
    }

    private List<StockPriceVO> fetchStockPriceFromWeb(String stockId) {
        List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
        try {

            // for normal company
            String queryStr = "cn_" + stockId;
            // for szzs, szcz, cybz
            if (stockId.equals(companyInfoHelper.getSZCZStockIdForDB())
                    || stockId.equals(companyInfoHelper.getCYBZStockIdForDB())) {
                queryStr = "zs_" + stockId;
            } else if (stockId.equals(companyInfoHelper.getSZZSStockIdForDB())) {
                // 999999 is db id, convert to 000001 in shohu
                queryStr = "zs_" + companyInfoHelper.getSZZSStockIdForSohu();
            }

            String url = baseUrl.replaceFirst("cn_stockId", queryStr);
            url = url.replaceFirst("startDate", this.startDate.replaceAll("-", ""));
            url = url.replaceFirst("endDate", this.endDate.replaceAll("-", ""));

            System.out.println("Fetch Sohu History Data for " + stockId);

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(10000);
            requestFactory.setReadTimeout(10000);

            if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
                Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(configure.getString(Constants.httpProxyServer),
                        configure.getInt(Constants.httpProxyPort)));
                requestFactory.setProxy(proxy);
            }

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            // System.out.println("url=" + urlStr.toString());
            String contents = restTemplate.getForObject(url.toString(), String.class).trim();

            if (Strings.isEmpty(contents) || contents.trim().length() <= 2) {
                System.out.println("Contents is empty");
                return spList;
            }

            // convert json to vo list
            // remove the outside []
            JSONObject jsonObject = JSONObject.fromObject(contents.substring(1, contents.length() - 1));
            SohuQuoteStockPriceVOWrap list = (SohuQuoteStockPriceVOWrap) JSONObject.toBean(jsonObject,
                    SohuQuoteStockPriceVOWrap.class, classMap);

            if (list == null || list.hq == null)
                return spList;

            for (int i = 0; i < list.hq.size(); i++) {
                String line = list.hq.get(i).toString();
                if (Strings.isNotEmpty(line)) {
                    String[] values = line.substring(1, line.length() - 1).split(",");
                    StockPriceVO spvo = new StockPriceVO();
                    spvo.stockId = stockId;
                    spvo.date = values[0].trim();
                    spvo.open = Double.parseDouble(values[1].trim());
                    spvo.close = Double.parseDouble(values[2].trim());
                    spvo.close = Double.parseDouble(values[2].trim());
                    spvo.low = Double.parseDouble(values[5].trim());
                    spvo.high = Double.parseDouble(values[6].trim());
                    spvo.lastClose = spvo.close - Double.parseDouble(values[3].trim());
                    spvo.volume = Long.parseLong(values[7].trim());

                    // System.out.println(spvo);
                    spList.add(spvo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return spList;
    }

    public void countAndSave(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("Process daily price for " + stockId + ", " + (++index) + " of " + stockIds.size());
            this.countAndSave(stockId);
        }
    }

    public void countAndSave(String stockId) {
        // fetch all history price from sohu api
        List<StockPriceVO> spList = this.fetchStockPriceFromWeb(stockId);
        if (spList.size() == 0) {
            System.out.println("Size for " + stockId + " is zero. Just return.");
            return;
        }
        // first delete all price for this stockId
        System.out
                .println("Delete stock price for " + stockId + " that between " + this.startDate + "~" + this.endDate);
        this.stockPriceTable.deleteBetweenDate(stockId, this.startDate, this.endDate);
        System.out.println("Save to database size=" + spList.size());
        // save to db
        for (StockPriceVO spvo : spList) {
            stockPriceTable.insert(spvo);
        }
    }

    public void reRunOnFailure() {
        List<String> stockIds = companyInfoTable.getAllCompanyStockId();
        for (String stockId : stockIds) {
            if (this.stockPriceTable.countTuplesByIDAndBetweenDate(stockId, "1997-01-01",
                    WeekdayUtil.currentDate()) <= 0) {
                System.out.println("Re run for " + stockId);
                this.countAndSave(stockId);
            }
        }
    }

    public static void main(String[] args) {
        String startDate = "1990-01-01";
        String endDate = WeekdayUtil.currentDate();

        if (args != null && args.length == 2) {
            startDate = args[0];
            endDate = args[1];
        }

        System.out.println("startDate=" + startDate + " and endDate=" + endDate);

        HistoryStockPriceDownloadAndStoreDBRunner runner = new HistoryStockPriceDownloadAndStoreDBRunner(startDate,
                endDate);
        List<String> stockIds = runner.companyInfoHelper.getAllStockId();
        // for major zhi shu
        // runner.countAndSave("999999");
        // runner.countAndSave("399001");
        // runner.countAndSave("399006");
        // for all stockIds
        runner.countAndSave(stockIds);
        // for specify stockId
        // runner.countAndSave("000049");

        // finally re run for failure
        // runner.reRunOnFailure();
    }
}
