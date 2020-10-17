package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.view.CommonViewHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//run analyse views and save selection to table checkpoint_daily_selection
@Component
public class DailyViewAnalyseRunner {
	private static Logger logger = LogHelper.getLogger(DailyViewAnalyseRunner.class);
	@Autowired
	@Qualifier("stockPriceTable")
	protected StockPriceTableHelper stockPriceTable;

	@Autowired
	private CommonViewHelper commonViewHelper;
	@Autowired
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable;

	// daily analyse, if miss one of date analyse, those view only have
	// latestDate date, there will be no chose to get that date's data
	private void slowAnalyseForView(String viewName) {
		String latestDate = stockPriceTable.getLatestStockDate();
		logger.debug("Analyse for viewName: " + viewName);
		List<String> stockIds = commonViewHelper.queryAllStockIds(viewName);
		
		stockIds.parallelStream().forEach(stockId -> {
          CheckPointDailySelectionVO cpvo = new CheckPointDailySelectionVO();
          cpvo.stockId = stockId;
          cpvo.checkPoint = viewName;
          cpvo.date = latestDate;

          checkPointDailySelectionTable.delete(stockId, latestDate, viewName);
          checkPointDailySelectionTable.insert(cpvo);
		});
		
//		for (String stockId : stockIds) {
//			CheckPointDailySelectionVO cpvo = new CheckPointDailySelectionVO();
//			cpvo.stockId = stockId;
//			cpvo.checkPoint = viewName;
//			cpvo.date = this.latestDate;
//
//			checkPointDailySelectionTable.delete(stockId, latestDate, viewName);
//			checkPointDailySelectionTable.insert(cpvo);
//		}
	}

	public void run() {
		logger.info("start DailyViewAnalyseRunner");
		long st = System.currentTimeMillis();
		//this.fastExtractForView("luzao_phaseII_zijinliu_top300");
		//this.fastExtractForView("luzao_phaseIII_zijinliu_top300");
		
		this.slowAnalyseForView("zijinliu_3_days_top300");
		this.slowAnalyseForView("zijinliu_3_of_5_days_top300");
		this.slowAnalyseForView("luzao_phaseIII_zijinliu_3_days_top300");
		this.slowAnalyseForView("luzao_phaseIII_zijinliu_3_of_5_days_top300");
		this.slowAnalyseForView("luzao_phaseII_zijinliu_3_days_top300");
		this.slowAnalyseForView("luzao_phaseII_zijinliu_3_of_5_days_top300");
		//this.slowAnalyseForView("luzao_phaseII_ddx_2_of_5_days_bigger_05");
		//this.slowAnalyseForView("luzao_phaseIII_ddx_2_of_5_days_bigger_05");
		
		this.slowAnalyseForView("luzao_phaseII_wr_all_ind_same");
		this.slowAnalyseForView("luzao_phaseIII_wr_all_ind_same");
		this.slowAnalyseForView("luzao_phaseIII_wr_midTerm_lonTerm_same");
		this.slowAnalyseForView("luzao_phaseII_wr_midTerm_lonTerm_same");
		this.slowAnalyseForView("luzao_phaseII_wr_shoTerm_midTerm_same");
		
		logger.info("stop DailyViewAnalyseRunner using " + (System.currentTimeMillis() - st) / (1000 * 60) + " mins");
	}
}
