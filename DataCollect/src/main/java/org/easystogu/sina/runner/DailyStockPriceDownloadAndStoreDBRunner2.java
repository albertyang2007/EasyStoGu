package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.CompanyInfoTableHelper;
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
			StockPriceVO vo = new StockPriceVO();
			vo.stockId = sqvo.code;
			vo.name = sqvo.name;
			// important: this json do not contain date information,
			// just time is not enough, so we must get it form hq.sinajs.cn
			vo.date = this.latestDate;
			vo.close = sqvo.trade;
			vo.open = sqvo.open;
			vo.low = sqvo.low;
			vo.high = sqvo.high;
			vo.volume = sqvo.volume / 100;// sina data is 100 then sohu history
											// data
			vo.lastClose = sqvo.trade - sqvo.pricechange;

			this.totalSize++;

			if (vo.isValidated()) {
				// System.out.println("saving into DB, vo=" + vo);
				stockPriceTable.delete(vo.stockId, vo.date);
				stockPriceTable.insert(vo);
			} else {
				System.out.println("vo invalidate: " + vo);
			}
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
