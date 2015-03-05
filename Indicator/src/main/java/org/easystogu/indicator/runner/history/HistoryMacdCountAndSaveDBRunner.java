package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.TALIBWraper;

//计算数据库中所有macd值，包括最新和历史的，一次性运行
public class HistoryMacdCountAndSaveDBRunner {

    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private TALIBWraper talib = new TALIBWraper();

    public void deleteMacd(String stockId) {
        macdTable.delete(stockId);
    }

    public void deleteMacd(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("Delete MACD for " + stockId + " " + (++index) + " of " + stockIds.size());
            this.deleteMacd(stockId);
        }
    }

    public void countAndSaved(String stockId) {
        try {
            List<StockPriceVO> list = stockPriceTable.getStockPriceById(stockId);

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

            for (index = list.size() - 1; index >= 0; index--) {
                double dif = macd[0][index];
                double dea = macd[1][index];
                double macdRtn = (dif - dea) * 2;
                // System.out.println("DIF=" + dif);
                // System.out.println("DEA=" + dea);
                // System.out.println("MACD=" + macdRtn);

                MacdVO macdVo = new MacdVO();
                macdVo.setStockId(stockId);
                macdVo.setDate(list.get(index).date);
                macdVo.setDif(dif);
                macdVo.setDea(dea);
                macdVo.setMacd(macdRtn);

                if (macdTable.getMacd(macdVo.stockId, macdVo.date) == null) {
                    macdTable.insert(macdVo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        HistoryMacdCountAndSaveDBRunner runner = new HistoryMacdCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
    }
}
