package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.sina.common.SinaQuoteStockPriceVO;
import org.easystogu.sina.helper.DailyStockPriceDownloadHelper2;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//daily get real time stock price from http://vip.stock.finance.sina.com.cn/quotes_service/api/
//it will get all the stockId from the web, including the new on board stockId
@Component
public class DailyStockPriceDownloadAndStoreDBRunner2 {
	@Autowired
    private CompanyInfoFileHelper stockConfig;
	@Autowired
	@Qualifier("stockPriceTable")
    private StockPriceTableHelper stockPriceTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
    private QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	private CompanyInfoTableHelper companyInfoTable;
	@Autowired
    private DailyStockPriceDownloadAndStoreDBRunner runner1;
	@Autowired
    private DailyStockPriceDownloadHelper2 sinaHelper2;
	@Autowired
    private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanRunner;

    // first download szzs, szcz, cybz,
    // must record the latest date time
    public String downloadMainBoardIndicator() {
        List<String> stockIds = new ArrayList<String>();

        stockIds.add(stockConfig.getSZZSStockIdForSina());
        stockIds.add(stockConfig.getSZCZStockIdForSina());
        stockIds.add(stockConfig.getCYBZStockIdForSina());

        runner1.downloadDataAndSaveIntoDB(stockIds);
        // important: this json do not contain date information,
        // just time is not enough, so we must get it form hq.sinajs.cn
        // then query the database and get the latest deal date
        String latestDate = stockPriceTable.getLatestStockDate();
        return latestDate;
    }

    public int downloadDataAndSaveIntoDB(String latestDate) {
    	int totalSize = 0;
        if (Strings.isEmpty(latestDate)) {
            System.out.println("Fatel Error, the latestDate is null! Return.");
            return 0;
        }

        System.out.println("Get stock price for " + latestDate);

        List<SinaQuoteStockPriceVO> sqsList = sinaHelper2.fetchAllStockPriceFromWeb();
        for (SinaQuoteStockPriceVO sqvo : sqsList) {
            // to check if the stockId is a new on board one, if so, insert to
            // companyInfo table
            CompanyInfoVO companyInfo = companyInfoTable.getCompanyInfoByStockId(sqvo.code);
            if (companyInfo == null) {
                CompanyInfoVO cinvo = new CompanyInfoVO(sqvo.code, sqvo.name);
                companyInfoTable.insert(cinvo);
                System.out.println("New company on board " + sqvo.code + " " + sqvo.name);
            } else if (Strings.isNotEmpty(companyInfo.name) && !companyInfo.name.equals(sqvo.name)) {
                //update the company name
                System.out.println("Company change name from " + companyInfo.name + " to " + sqvo.name);
                companyInfo.name = sqvo.name;
                companyInfoTable.updateName(companyInfo);
            }
            // convert to stockprice and save to DB
            this.saveIntoDB(sqvo, latestDate, totalSize);
        }
        
        return totalSize;
    }

    public void saveIntoDB(SinaQuoteStockPriceVO sqvo, String latestDate , int totalSize) {
        try {
            // update stockprice into table
            StockPriceVO spvo = new StockPriceVO();
            spvo.stockId = sqvo.code;
            spvo.name = sqvo.name;
            // important: this json do not contain date information,
            // just time is not enough, so we must get it form hq.sinajs.cn
            spvo.date = latestDate;
            spvo.close = sqvo.trade;
            spvo.open = sqvo.open;
            spvo.low = sqvo.low;
            spvo.high = sqvo.high;
            // sina data is 100 then sohu history data
            spvo.volume = sqvo.volume / 100;
            spvo.lastClose = sqvo.trade - sqvo.pricechange;

            // delete if today old data is exist
            this.stockPriceTable.delete(spvo.stockId, spvo.date);
            List<StockPriceVO> nDaySpList = this.stockPriceTable.getNdateStockPriceById(spvo.stockId, 1);
            // System.out.println("saving into DB, vo=" + vo);
            this.stockPriceTable.insert(spvo);
            // also insert the qian fuquan stockprice
            this.qianFuQuanStockPriceTable.delete(spvo.stockId, spvo.date);
            this.qianFuQuanStockPriceTable.insert(spvo);

            // check if chu quan event exist
            if (nDaySpList.size() > 0) {
                StockPriceVO prevo = nDaySpList.get(0);
                if (spvo.lastClose != 0 && prevo.close != 0 && spvo.lastClose != prevo.close) {
                    double rate = prevo.close / spvo.lastClose;
                    if (rate <= 0.95 || rate >= 1.05) {
                        // chu quan event
                        System.out.println("Chu Quan happens for " + spvo.stockId + ", rate=" + rate);
                        this.historyQianFuQuanRunner.countAndSave(spvo.stockId);
                    }
                }
            }

            totalSize++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
    	String latestDate = downloadMainBoardIndicator();
        int totalSize = downloadDataAndSaveIntoDB(latestDate);
        System.out.println("\ntotalSize=" + totalSize);
    }

    public void mainWork(String[] args) {
        this.run();
    }
}
