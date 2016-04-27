package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.CompanyInfoTableHelper;
import org.easystogu.db.access.FuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.sina.common.SinaQuoteStockPriceVO;
import org.easystogu.sina.helper.DailyStockPriceDownloadHelper2;
import org.easystogu.utils.Strings;

//daily get real time stock price from http://vip.stock.finance.sina.com.cn/quotes_service/api/
//it will get all the stockId from the web, including the new on board stockId
public class DailyStockPriceDownloadAndStoreDBRunner2 implements Runnable {

	// private static Logger logger =
	// LogHelper.getLogger(DailyStockPriceDownloadAndStoreDBRunner2.class);
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private FuQuanStockPriceTableHelper fuquanStockPriceTable = FuQuanStockPriceTableHelper.getInstance();
	private CompanyInfoTableHelper companyInfoTable = CompanyInfoTableHelper.getInstance();
	private DailyStockPriceDownloadAndStoreDBRunner runner1 = new DailyStockPriceDownloadAndStoreDBRunner();
	private DailyStockPriceDownloadHelper2 sinaHelper2 = new DailyStockPriceDownloadHelper2();
	private String latestDate = "";
	private int totalSize = 0;

	// first download szzs, szcz, cybz,
	// must record the latest date time
	public void downloadMainBoardIndicator() {
		List<String> stockIds = new ArrayList<String>();

		stockIds.add(stockConfig.getSZZSStockIdForSina());
		stockIds.add(stockConfig.getSZCZStockIdForSina());
		stockIds.add(stockConfig.getCYBZStockIdForSina());

		runner1.downloadDataAndSaveIntoDB(stockIds);
		// important: this json do not contain date information,
		// just time is not enough, so we must get it form hq.sinajs.cn
		// then query the database and get the latest deal date
		this.latestDate = stockPriceTable.getLatestStockDate();
	}

	public void downloadDataAndSaveIntoDB() {

		if (Strings.isEmpty(this.latestDate)) {
			System.out.println("Fatel Error, the latestDate is null! Return.");
			return;
		}

		System.out.println("Get stock price for " + this.latestDate);

		List<SinaQuoteStockPriceVO> sqsList = sinaHelper2.fetchAllStockPriceFromWeb();
		for (SinaQuoteStockPriceVO sqvo : sqsList) {
			// to check if the stockId is a new on board one, if so, insert to
			// companyInfo table
			if (companyInfoTable.getCompanyInfoByStockId(sqvo.code) == null) {
				CompanyInfoVO cinvo = new CompanyInfoVO(sqvo.code, sqvo.name);
				companyInfoTable.insert(cinvo);
				System.out.println("New company on board " + sqvo.code + " " + sqvo.name);
			}
			// convert to stockprice and save to DB
			this.saveIntoDB(sqvo);
		}
	}

	public void saveIntoDB(SinaQuoteStockPriceVO sqvo) {
		try {
			// update stock price into table
			StockPriceVO spvo = new StockPriceVO();
			spvo.stockId = sqvo.code;
			spvo.name = sqvo.name;
			// important: this json do not contain date information,
			// just time is not enough, so we must get it form hq.sinajs.cn
			spvo.date = this.latestDate;
			spvo.close = sqvo.trade;
			spvo.open = sqvo.open;
			spvo.low = sqvo.low;
			spvo.high = sqvo.high;
			spvo.volume = sqvo.volume / 100;// sina data is 100 then sohu
											// history
											// data
			spvo.lastClose = sqvo.trade - sqvo.pricechange;

			// delete if today old data is exist
			this.stockPriceTable.delete(spvo.stockId, spvo.date);
			StockPriceVO yesterday_spvo = this.stockPriceTable.getNdateStockPriceById(spvo.stockId, 1).get(0);

			// System.out.println("saving into DB, vo=" + vo);
			this.stockPriceTable.insert(spvo);

			boolean chuQuanEvent = false;
			// if lastClose is not equal, then chuQuan happends!
			// is it enough to check this chuQuan ???
			// condiction: +-10% zhang die ting ban
			double newRate = 1.0;
			if ((spvo.close > 0 && yesterday_spvo.close > 0) && (spvo.close / yesterday_spvo.close <= 0.85)) {
				chuQuanEvent = true;
				newRate = yesterday_spvo.close / spvo.lastClose;
			}

			if (chuQuanEvent) {
				System.out.println("Import chuQuan event for " + spvo.stockId + ", new rate=" + newRate
						+ ", must manually update fuquan StockPrice!!!");
			}

			// update fuquan stockprice table by manually, assume there is no
			// chuquan event at this date
			// delete if today old data is exist
			this.fuquanStockPriceTable.delete(spvo.stockId, spvo.date);
			StockPriceVO yesterday_fqspvo = this.fuquanStockPriceTable.getNdateStockPriceById(spvo.stockId, 1).get(0);

			double lastRate = yesterday_fqspvo.close / yesterday_spvo.close;
			
			StockPriceVO fqspvo = new StockPriceVO();
			fqspvo.date = spvo.date;
			fqspvo.stockId = spvo.stockId;
			fqspvo.close = Strings.convert2ScaleDecimal(spvo.close * lastRate * newRate);
			fqspvo.open = Strings.convert2ScaleDecimal(spvo.open * lastRate * newRate);
			fqspvo.low = Strings.convert2ScaleDecimal(spvo.low * lastRate * newRate);
			fqspvo.high = Strings.convert2ScaleDecimal(spvo.high * lastRate * newRate);
			fqspvo.volume = spvo.volume;

			System.out.println("saving fuquan into DB, vo=" + fqspvo);
			this.fuquanStockPriceTable.insert(fqspvo);

			this.totalSize++;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		downloadMainBoardIndicator();
		downloadDataAndSaveIntoDB();
		System.out.println("\ntotalSize=" + this.totalSize);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailyStockPriceDownloadAndStoreDBRunner2 runner = new DailyStockPriceDownloadAndStoreDBRunner2();
		runner.run();
	}
}
