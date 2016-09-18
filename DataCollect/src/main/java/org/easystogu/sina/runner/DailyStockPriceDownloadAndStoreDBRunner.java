package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.DailyStockPriceDownloadHelper;

//daily get real time stock price from http://hq.sinajs.cn/list=
//it need sotckIds as parameter
public class DailyStockPriceDownloadAndStoreDBRunner implements Runnable {
    // private static Logger logger =
    // LogHelper.getLogger(DailyStockPriceDownloadAndStoreDBRunner.class);
    private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    //private StockPriceTableHelper houFuQuanStockPriceTable = HouFuQuanStockPriceTableHelper.getInstance();
    private DailyStockPriceDownloadHelper sinaHelper = new DailyStockPriceDownloadHelper();
    private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
    private int totalError = 0;
    private int totalSize = 0;

    public void downloadDataAndSaveIntoDB(List<String> allStockIds) {
        int batchSize = 200;
        int batchs = allStockIds.size() / batchSize;
        totalSize = allStockIds.size();
        System.out.println("Process daily price, totalSize= " + totalSize);
        // 分批取数据
        int index = 0;
        for (; index < batchs; index++) {
            System.out.println("Process daily price " + index + "/" + batchs);
            List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(allStockIds.subList(index * batchSize, (index + 1)
                    * batchSize));
            for (RealTimePriceVO vo : list) {
                this.saveIntoDB(vo.convertToStockPriceVO());
            }
        }
        // 去剩余数据
        System.out.println("Process daily price " + index + "/" + batchs);
        List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(allStockIds.subList(index * batchSize,
                allStockIds.size()));
        for (RealTimePriceVO vo : list) {
            this.saveIntoDB(vo.convertToStockPriceVO());
        }
    }

    public void saveIntoDB(StockPriceVO vo) {
        try {
            if (vo.isValidated()) {
                // System.out.println("saving into DB, vo=" + vo);
                stockPriceTable.delete(vo.stockId, vo.date);
                qianFuQuanStockPriceTable.delete(vo.stockId, vo.date);
                //houFuQuanStockPriceTable.delete(vo.stockId, vo.date);

                if (vo.stockId.equals(this.companyInfoHelper.getSZCZStockIdForDB())
                        || vo.stockId.equals(this.companyInfoHelper.getCYBZStockIdForDB()))
                    vo.volume = vo.volume / 100;

                stockPriceTable.insert(vo);
                qianFuQuanStockPriceTable.insert(vo);
                //houFuQuanStockPriceTable.insert(vo);
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
        List<String> shStockIds = stockConfig.getAllSHStockId("sh");
        List<String> szStockIds = stockConfig.getAllSZStockId("sz");

        List<String> allStockIds = new ArrayList<String>();

        allStockIds.add(stockConfig.getSZZSStockIdForSina());
        allStockIds.add(stockConfig.getSZCZStockIdForSina());
        allStockIds.add(stockConfig.getCYBZStockIdForSina());
        allStockIds.addAll(shStockIds);
        allStockIds.addAll(szStockIds);

        downloadDataAndSaveIntoDB(allStockIds);
        printResult();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DailyStockPriceDownloadAndStoreDBRunner runner = new DailyStockPriceDownloadAndStoreDBRunner();
        runner.run();
    }
}
