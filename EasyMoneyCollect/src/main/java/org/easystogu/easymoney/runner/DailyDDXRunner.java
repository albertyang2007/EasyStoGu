package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.db.access.IndDDXTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.DDXVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class DailyDDXRunner implements Runnable {
    private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
    private ZiJinLiuTableHelper zijinliuTableHelper = ZiJinLiuTableHelper.getInstance();
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private IndDDXTableHelper ddxTable = IndDDXTableHelper.getInstance();
    private String latestDate = stockPriceTable.getLatestStockDate();
    private int count = 0;

    public void countAndSaved(String stockId) {
        ZiJinLiuVO zjlvo = zijinliuTableHelper.getZiJinLiu(stockId, latestDate);
        if (zjlvo == null)
            return;

        StockPriceVO spvo = stockPriceTable.getStockPriceByIdAndDate(stockId, latestDate);
        if (spvo == null)
            return;

        CompanyInfoVO civo = stockConfig.getByStockId(stockId);
        if (civo == null)
            return;

        if (civo.liuTongAGu <= 0)
            return;

        DDXVO ddxvo = new DDXVO();
        ddxvo.stockId = stockId;
        ddxvo.date = latestDate;
        ddxvo.ddx = zjlvo.getMajorNetIn() / (civo.liuTongAGu * spvo.close) / 100;
        // System.out.println(ddxvo);
        ddxTable.insert(ddxvo);
        count++;
    }

    public void countAndSaved(List<String> stockIds) {
        ddxTable.deleteByDate(latestDate);
        for (String stockId : stockIds) {
            this.countAndSaved(stockId);
        }
        System.out.println("Total count DDX:" + count);
    }

    public void run() {
        countAndSaved(stockConfig.getAllStockId());
    }

    public static void main(String[] args) {
        DailyDDXRunner runner = new DailyDDXRunner();
        runner.run();
    }
}
