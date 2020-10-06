package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.table.ScheduleActionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.ScheduleActionVO;
import org.easystogu.indicator.runner.history.IndicatorHistortOverAllRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryWeekStockPriceCountAndSaveDBRunner;
import org.easystogu.utils.WeekdayUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailyScheduleActionRunner {
	private static Logger logger = LogHelper.getLogger(DailyScheduleActionRunner.class);
	@Autowired
	private ScheduleActionTableHelper scheduleActionTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper qianfuquanStockPriceTable;
	@Autowired
	private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanRunner;
	@Autowired
	private IndicatorHistortOverAllRunner indicatorHistoryRunner;
	@Autowired
	private HistoryWeekStockPriceCountAndSaveDBRunner weekPriceHistoryRunner;
	@Autowired
	private HistoryStockPriceDownloadAndStoreDBRunner priceHistoryRunner;

	public void runAllScheduleAction() {
		String currentDate = WeekdayUtil.currentDate();
		List<ScheduleActionVO> actions = this.scheduleActionTable.getAllShouldRunDate(currentDate);
		actions.parallelStream().forEach(savo -> {
			// for (ScheduleActionVO savo : actions) {

			if (currentDate.compareTo(savo.getRunDate()) >= 0) {

				if (savo.actionDo.equals(ScheduleActionVO.ActionDo.refresh_history_stockprice.name())) {
					logger.debug("refresh_history_stockprice for " + savo.stockId);
					// fetch original history data
					this.priceHistoryRunner.countAndSave(savo.stockId, "2000-01-01", currentDate);
					// for qian fuquan history data
					this.historyQianFuQuanRunner.countAndSave(savo.stockId);
					// delete schedule action if success
					if (this.qianfuquanStockPriceTable.countByStockId(savo.stockId) > 0) {
						this.scheduleActionTable.delete(savo.stockId, savo.actionDo);
					}

					// update week price
					weekPriceHistoryRunner.countAndSave(savo.stockId);
					// update indicator
					indicatorHistoryRunner.countAndSave(savo.stockId);
				} else if (savo.actionDo.equals(ScheduleActionVO.ActionDo.refresh_fuquan_history_stockprice.name())) {
					logger.debug("refresh_fuquan_history_stockprice for " + savo.stockId);
					// fetch hou ququan history data
					// this.historyHouFuQuanRunner.countAndSave(savo.stockId);
					// for qian fuquan
					this.historyQianFuQuanRunner.countAndSave(savo.stockId);
					// delete schedule action if success
					if (this.qianfuquanStockPriceTable.countByStockId(savo.stockId) > 0) {
						this.scheduleActionTable.delete(savo.stockId, savo.actionDo);
					}

					// update week price
					weekPriceHistoryRunner.countAndSave(savo.stockId);
					// update indicator
					indicatorHistoryRunner.countAndSave(savo.stockId);
				}
			}
		});
	}

	public void run() {
		this.runAllScheduleAction();
	}
}
