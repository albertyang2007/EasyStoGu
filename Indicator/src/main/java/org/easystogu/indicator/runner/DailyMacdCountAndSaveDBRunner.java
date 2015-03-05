package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.TALIBWraper;

//每日根据最新数据计算当天的macd值，每天运行一次
public class DailyMacdCountAndSaveDBRunner {
    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected TALIBWraper talib = new TALIBWraper();

    public void deleteMacd(String stockId, String date) {
        macdTable.delete(stockId, date);
    }

    public void countAndSaved(String stockId) {

        List<StockPriceVO> list = stockPriceTable.getStockPriceById(stockId);
        // List<StockPriceVO> list =
        // stockPriceTable.getNdateStockPriceById(stockId, minLength);
        // Collections.reverse(list);

        int length = list.size();

        if (length < 26) {
            System.out.println(stockId
                    + " price data is not enough to count MACD, please wait until it has at least 26 days. Skip");
            return;
        }

        double[] close = new double[length];
        int index = 0;
        for (StockPriceVO vo : list) {
            close[index++] = vo.close;
        }

        double[][] macd = talib.getMacdExt(close, 12, 26, 9);

        index = list.size() - 1;
        double dif = macd[0][index];
        double dea = macd[1][index];
        double macdRtn = (dif - dea) * 2;
        //System.out.println("date=" + list.get(index).date);
        //System.out.println("DIF=" + dif);
        //System.out.println("DEA=" + dea);
        //System.out.println("MACD=" + macdRtn);

        MacdVO vo = new MacdVO();
        vo.setStockId(stockId);
        vo.setDate(list.get(index).date);
        vo.setDif(dif);
        vo.setDea(dea);
        vo.setMacd(macdRtn);

        this.deleteMacd(stockId, vo.date);
        macdTable.insert(vo);

    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("MACD countAndSaved: " + stockId + " " + (++index) + "/" + stockIds.size());
            this.countAndSaved(stockId);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        DailyMacdCountAndSaveDBRunner runner = new DailyMacdCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600825");
    }
}
