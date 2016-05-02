package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.FuQuanStockPriceTableHelper;
import org.easystogu.db.access.ScheduleActionTableHelper;
import org.easystogu.db.table.ScheduleActionVO;
import org.easystogu.sina.runner.history.HistoryFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.utils.WeekdayUtil;

public class DailyScheduleActionRunner implements Runnable {
	private String currentDate = WeekdayUtil.currentDate();
	private ScheduleActionTableHelper scheduleActionTable = ScheduleActionTableHelper.getInstance();
	private FuQuanStockPriceTableHelper fuquanStockPriceTable = FuQuanStockPriceTableHelper.getInstance();
	private HistoryFuQuanStockPriceDownloadAndStoreDBRunner historyFuQuanRunner = new HistoryFuQuanStockPriceDownloadAndStoreDBRunner();

	public void runAllScheduleAction() {
		List<ScheduleActionVO> actions = this.scheduleActionTable.getAllShouldRunDate(currentDate);
		for (ScheduleActionVO savo : actions) {
			if (savo.actionDo.equals(ScheduleActionVO.ActionDo.refresh_fuquan_history_stockprice.name())) {
				System.out.println("refresh_fuquan_history_stockprice for " + savo.stockId);
				// fetch ququan history data
				this.historyFuQuanRunner.countAndSave(savo.stockId);
				// delete schedule action if success
				if (this.fuquanStockPriceTable.countByStockId(savo.stockId) > 0) {
					this.scheduleActionTable.delete(savo.stockId, savo.runDate, savo.actionDo);
				}
			} else {
				// for other actions
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
