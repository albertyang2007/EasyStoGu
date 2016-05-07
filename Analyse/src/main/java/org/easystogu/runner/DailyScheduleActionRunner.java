package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.HouFuQuanStockPriceTableHelper;
import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.ScheduleActionTableHelper;
import org.easystogu.db.table.ScheduleActionVO;
import org.easystogu.indicator.runner.history.IndicatorHistortOverAllRunner;
import org.easystogu.sina.runner.history.HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.utils.WeekdayUtil;

public class DailyScheduleActionRunner implements Runnable {
    private String currentDate = WeekdayUtil.currentDate();
    private ScheduleActionTableHelper scheduleActionTable = ScheduleActionTableHelper.getInstance();
    private HouFuQuanStockPriceTableHelper houfuquanStockPriceTable = HouFuQuanStockPriceTableHelper.getInstance();
    private QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    private HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner historyHouFuQuanRunner = new HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner();
    private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanRunner = new HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner();
    private IndicatorHistortOverAllRunner indicatorHistoryRunner = new IndicatorHistortOverAllRunner();
    public void runAllScheduleAction() {
        List<ScheduleActionVO> actions = this.scheduleActionTable.getAllShouldRunDate(currentDate);
        for (ScheduleActionVO savo : actions) {
            if (savo.actionDo.equals(ScheduleActionVO.ActionDo.refresh_fuquan_history_stockprice.name())) {
                System.out.println("refresh_hou_fuquan_history_stockprice for " + savo.stockId);
                // fetch hou ququan history data
                this.historyHouFuQuanRunner.countAndSave(savo.stockId);
                // delete schedule action if success
                if (this.houfuquanStockPriceTable.countByStockId(savo.stockId) > 0) {
                    this.scheduleActionTable.delete(savo.stockId, savo.runDate, savo.actionDo);
                }
                //for qian fuquan 
                this.historyQianFuQuanRunner.countAndSave(savo.stockId);
                // delete schedule action if success
                if (this.qianfuquanStockPriceTable.countByStockId(savo.stockId) > 0) {
                    this.scheduleActionTable.delete(savo.stockId, savo.runDate, savo.actionDo);
                }

                //update indicator
                indicatorHistoryRunner.countAndSave(savo.stockId);
            }
        }
    }

    public void run() {
        this.runAllScheduleAction();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DailyScheduleActionRunner runner = new DailyScheduleActionRunner();
        runner.run();
    }
}
