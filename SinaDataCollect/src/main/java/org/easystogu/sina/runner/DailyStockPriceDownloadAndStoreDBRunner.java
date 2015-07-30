package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.SinaDataDownloadHelper;

public class DailyStockPriceDownloadAndStoreDBRunner implements Runnable {
    //private static Logger logger = LogHelper.getLogger(DailyStockPriceDownloadAndStoreDBRunner.class);
    private StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
    private StockPriceTableHelper tableHelper = StockPriceTableHelper.getInstance();
    private SinaDataDownloadHelper sinaHelper = new SinaDataDownloadHelper();
    private int totalError = 0;
    private int totalSize = 0;

    public void downloadDataAndSaveIntoDB() {
        List<String> shStockIds = stockConfig.getAllSHStockId("sh");
        List<String> szStockIds = stockConfig.getAllSZStockId("sz");
        String szzsStockId = stockConfig.getSZZSStockId();

        List<String> totalStockIds = new ArrayList<String>();

        totalStockIds.add(szzsStockId);
        totalStockIds.addAll(shStockIds);
        totalStockIds.addAll(szStockIds);

        int batchSize = 200;
        int batchs = totalStockIds.size() / batchSize;
        totalSize = totalStockIds.size();

        // 分批取数据
        int index = 0;
        for (; index < batchs; index++) {
            System.out.println("Process daily price " + index + "/" + batchs);
            List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(totalStockIds.subList(index * batchSize,
                    (index + 1) * batchSize));
            for (RealTimePriceVO vo : list) {
                this.saveIntoDB(vo.convertToStockPriceVO());
            }
        }
        // 去剩余数据
        System.out.println("Process daily price " + index + "/" + batchs);
        List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(totalStockIds.subList(index * batchSize,
                totalStockIds.size()));
        for (RealTimePriceVO vo : list) {
            this.saveIntoDB(vo.convertToStockPriceVO());
        }
    }

    public void saveIntoDB(StockPriceVO vo) {
        try {
        	if(vo.stockId.equalsIgnoreCase("sh000001"))
        		System.out.println("szzs" + vo);
            if (vo.isValidated()) {
                //System.out.println("saving into DB, vo=" + vo);
                tableHelper.delete(vo.stockId, vo.date);
                tableHelper.insert(vo);
            }else {
				System.out.println("vo invalidate: " + vo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Can't save to DB, vo=" + vo + ", error=" + e.getMessage());
            e.printStackTrace();
            totalError++;
            //logger.error("Can not save stock price to DB " + vo.toString(), e);
        }
    }

    private void printResult() {
        System.out.println("totalSize=" + this.totalSize);
        System.out.println("totalError=" + this.totalError);
    }

    public void run() {
        downloadDataAndSaveIntoDB();
        printResult();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DailyStockPriceDownloadAndStoreDBRunner runner = new DailyStockPriceDownloadAndStoreDBRunner();
        runner.downloadDataAndSaveIntoDB();
        runner.printResult();
    }
}
