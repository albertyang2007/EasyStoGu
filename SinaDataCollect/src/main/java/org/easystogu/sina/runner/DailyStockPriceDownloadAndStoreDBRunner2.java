package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.CompanyInfoTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.sina.common.SinaQuotesServiceVO;
import org.easystogu.sina.helper.DailyStockPriceDownloadHelper2;
import org.easystogu.utils.WeekdayUtil;

//daily get real time stock price from http://vip.stock.finance.sina.com.cn/quotes_service/api/
//it will get all the stockId from the web, including the new on board stockId
public class DailyStockPriceDownloadAndStoreDBRunner2 implements Runnable {

    // private static Logger logger =
    // LogHelper.getLogger(DailyStockPriceDownloadAndStoreDBRunner2.class);
    private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private CompanyInfoTableHelper companyInfoTable = CompanyInfoTableHelper.getInstance();
    private DailyStockPriceDownloadHelper2 sinaHelper2 = new DailyStockPriceDownloadHelper2();
    private int totalError = 0;
    private int totalSize = 0;

    public void downloadDataAndSaveIntoDB() {
        List<SinaQuotesServiceVO> sqsList = sinaHelper2.fetchAllStockPriceFromWeb();
        for (SinaQuotesServiceVO sqvo : sqsList) {
            //to check if the stockId is a new on board one, if so, insert to companyInfo table
            if (companyInfoTable.getCompanyId(sqvo.code) == null) {
                CompanyInfoVO cinvo = new CompanyInfoVO(sqvo.code, sqvo.name);
                companyInfoTable.insert(cinvo);
            }
            //update stock price into table
            StockPriceVO spvo = new StockPriceVO();
            spvo.stockId = sqvo.code;
            spvo.name = sqvo.name;
            spvo.date = WeekdayUtil.currentDate();
            spvo.close = sqvo.trade;
            spvo.open = sqvo.open;
            spvo.low = sqvo.low;
            spvo.high = sqvo.high;
            spvo.volume = sqvo.volume;
            spvo.lastClose = sqvo.trade - sqvo.pricechange;
            
            this.saveIntoDB(spvo);
        }
    }

    public void saveIntoDB(StockPriceVO vo) {
        try {
            if (vo.isValidated()) {
                System.out.println("saving into DB, vo=" + vo);
                stockPriceTable.delete(vo.stockId, vo.date);
                stockPriceTable.insert(vo);
            } else {
                System.out.println("vo invalidate: " + vo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Can't save to DB, vo=" + vo + ", error=" + e.getMessage());
            e.printStackTrace();
            totalError++;
            // logger.error("Can not save stock price to DB " + vo.toString(),
            // e);
        }
    }

    private void printResult() {
        System.out.println("totalSize=" + this.totalSize);
        System.out.println("totalError=" + this.totalError);
    }

    public void run() {
        List<String> allStockIds = new ArrayList<String>();

        allStockIds.add(stockConfig.getSZZSStockIdForSina());
        allStockIds.add(stockConfig.getSZCZStockIdForSina());
        allStockIds.add(stockConfig.getCYBZStockIdForSina());

        downloadDataAndSaveIntoDB();
        printResult();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DailyStockPriceDownloadAndStoreDBRunner2 runner = new DailyStockPriceDownloadAndStoreDBRunner2();
        runner.run();
    }
}
