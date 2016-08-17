package org.easystogu.sina.runner.history;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CompanyInfoTableHelper;
import org.easystogu.db.access.HouFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//never use hou fu quan stockproce now
//get hou fuquan history stock price from sina
public class HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner {
	private static String baseUrl = "http://vip.stock.finance.sina.com.cn/api/json_v2.php/BasicStockSrv.getStockFuQuanData?symbol=stockId&type=hfq";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();
	private HouFuQuanStockPriceTableHelper houfuquanStockPriceTable = HouFuQuanStockPriceTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CompanyInfoTableHelper companyInfoTable = CompanyInfoTableHelper.getInstance();
	private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();

	public List<StockPriceVO> fetchFuQuanStockPriceFromWeb(List<String> stockIds) {
		List<StockPriceVO> list = new ArrayList<StockPriceVO>();
		for (String stockId : stockIds) {
			list.addAll(this.fetchFuQuanStockPriceFromWeb(stockId));
		}
		return list;
	}

	private List<StockPriceVO> fetchFuQuanStockPriceFromWeb(String stockId) {
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		try {

			String url = baseUrl.replaceFirst("stockId", stockId);
			System.out.println("Fetch Sina FuQuan Data for " + stockId);

			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(10000);
			requestFactory.setReadTimeout(10000);

			if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
						configure.getString(Constants.httpProxyServer), configure.getInt(Constants.httpProxyPort)));
				requestFactory.setProxy(proxy);
			}

			RestTemplate restTemplate = new RestTemplate(requestFactory);

			// System.out.println("url=" + urlStr.toString());
			String contents = restTemplate.getForObject(url.toString(), String.class);

			if (Strings.isEmpty(contents)) {
				System.out.println("Contents is empty");
				return spList;
			}

			// convert json to vo list
			// remove the outside ()
			contents = contents.substring(1, contents.length() - 1);
			// System.out.println(contents);
			// extract only data value
			contents = contents.split("data:")[1];
			// remove outside {}
			contents = contents.substring(1, contents.length() - 2);
			// System.out.println(contents);
			// one item is : _2016_04_25:"80.3423"
			String[] items = contents.split(",");

			for (int i = 0; i < items.length; i++) {
				// System.out.println(items[i]);
				String[] tmp = items[i].split(":");
				StockPriceVO spvo = new StockPriceVO();
				spvo.stockId = stockId;
				spvo.date = tmp[0].substring(1, tmp[0].length()).replaceAll("_", "-");
				spvo.close = this.parseDouble(tmp[1].replaceAll("\"", ""));
				// System.out.println(spvo);
				spList.add(spvo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return spList;
	}

	private double parseDouble(String s) {
		if (Strings.isNotEmpty(s) && !"null".equals(s)) {
			return Double.parseDouble(s);
		}
		return 0.0;
	}

	public void countAndSave(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Process hou fuquan price for " + stockId + ", " + (++index) + " of " + stockIds.size());
			this.countAndSave(stockId);
		}
	}

	public void countAndSave(String stockId) {
		// first delete all price for this stockId
		System.out.println("Delete hou fuquan stock price for " + stockId);
		this.houfuquanStockPriceTable.delete(stockId);
		// fetch all history price from sohu api
		// FuQuan data is in date order desc
		List<StockPriceVO> fqspList = this.fetchFuQuanStockPriceFromWeb(stockId);
		System.out.println("Save to database size=" + fqspList.size());
		// save to db
		for (StockPriceVO fqspvo : fqspList) {
			StockPriceVO spvo = this.stockPriceTable.getStockPriceByIdAndDate(stockId, fqspvo.date);
			if (spvo != null) {
				double rate = fqspvo.close / spvo.close;
				fqspvo.close = Strings.convert2ScaleDecimal(fqspvo.close);
				fqspvo.open = Strings.convert2ScaleDecimal(spvo.open * rate);
				fqspvo.low = Strings.convert2ScaleDecimal(spvo.low * rate);
				fqspvo.high = Strings.convert2ScaleDecimal(spvo.high * rate);
				fqspvo.volume = spvo.volume;
				houfuquanStockPriceTable.insert(fqspvo);
			} else {
				// sohu data is not so correct, shift!!!
				// System.out.println("Missging StockPrice for " + stockId +
				// " at " + fqspvo.date);
			}
		}
	}

	// just copy stockprice to hou fuquan stockprice for 999999 etc
	public void countAndSavevForMajorIndicator() {
		try {
			List<String> stockIds = new ArrayList<String>();
			stockIds.add(companyInfoHelper.getSZCZStockIdForDB());
			stockIds.add(companyInfoHelper.getCYBZStockIdForDB());
			stockIds.add(companyInfoHelper.getSZZSStockIdForDB());

			for (String stockId : stockIds) {
				this.houfuquanStockPriceTable.delete(stockId);
				this.houfuquanStockPriceTable.insert(stockPriceTable.getStockPriceById(stockId));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reRunOnFailure() {
		List<String> stockIds = companyInfoTable.getAllCompanyStockId();
		for (String stockId : stockIds) {
			if (this.houfuquanStockPriceTable.countTuplesByIDAndBetweenDate(stockId, "1997-01-01",
					WeekdayUtil.currentDate()) <= 0) {
				System.out.println("Re run for " + stockId);
				this.countAndSave(stockId);
			}
		}
	}

	public static void main(String[] args) {
		HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner runner = new HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner();
		List<String> stockIds = runner.companyInfoTable.getAllCompanyStockId();
		// for all stockIds
		runner.countAndSave(stockIds);
		// for specify stockId
		//runner.countAndSave("601388");

		// for major indicator
		runner.countAndSavevForMajorIndicator();
		// finally re run for failure
		// runner.reRunOnFailure();
	}
}
