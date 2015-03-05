package org.easystogu.indicator.runner;

import java.util.Collections;
import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.TALIBWraper;

//每日根据最新数据计算当天的boll值，每天运行一次
public class DailyBollCountAndSaveDBRunner {
    protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private TALIBWraper talib = new TALIBWraper();

    public void deleteBoll(String stockId, String date) {
        bollTable.delete(stockId, date);
    }

    public void countAndSaved(String stockId) {

        // List<StockPriceVO> list =
        // stockPriceTable.getStockPriceById(stockId);
        List<StockPriceVO> list = stockPriceTable.getNdateStockPriceById(stockId, 20);
        Collections.reverse(list);

        int length = list.size();

        if (length < 20) {
            System.out.println(stockId
                    + " price data is not enough to count Boll, please wait until it has at least 20 days. Skip");
            return;
        }

        double[] close = new double[length];
        int index = 0;
        for (StockPriceVO vo : list) {
            close[index++] = vo.close;
        }

        double[][] boll = talib.getBbands(close, 20, 2, 2);

        index = list.size() - 1;
        double up = boll[0][index];
        double mb = boll[1][index];
        double dn = boll[2][index];
        // System.out.println("MB=" + mb);
        // System.out.println("UP=" + up);
        // System.out.println("DN=" + dn);

        BollVO vo = new BollVO();
        vo.setStockId(stockId);
        vo.setDate(list.get(index).date);
        vo.setMb(mb);
        vo.setUp(up);
        vo.setDn(dn);

        this.deleteBoll(stockId, vo.date);
        bollTable.insert(vo);
    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("Boll countAndSaved: " + stockId + " " + (++index) + "/" + stockIds.size());
            this.countAndSaved(stockId);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        DailyBollCountAndSaveDBRunner runner = new DailyBollCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600825");
    }

}
