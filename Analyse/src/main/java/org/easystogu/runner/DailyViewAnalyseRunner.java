package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.view.CommonViewHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.view.CommonViewVO;

//run analyse views and save selection to table checkpoint_daily_selection
public class DailyViewAnalyseRunner implements Runnable {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private CommonViewHelper commonViewHelper = CommonViewHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();

	// daily analyse, if miss one of date analyse, those view only have
	// latestDate date, there will be no chose to get that date's data
	private void slowAnalyseForView(String viewName) {
		System.out.println("Analyse for viewName: " + viewName);
		List<String> stockIds = commonViewHelper.queryAllStockIds(viewName);
		
		stockIds.parallelStream().forEach(stockId -> {
          CheckPointDailySelectionVO cpvo = new CheckPointDailySelectionVO();
          cpvo.stockId = stockId;
          cpvo.checkPoint = viewName;
          cpvo.date = this.latestDate;

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

	// extract the currentDate from view, those view has many date's date
	private void fastExtractForView(String viewName) {
		System.out.println("Extract for viewName: " + viewName);
		List<CommonViewVO> list = commonViewHelper.queryByDateForCheckPoint(viewName, this.latestDate);
		for (CommonViewVO vo : list) {
			CheckPointDailySelectionVO cpvo = new CheckPointDailySelectionVO();
			cpvo.stockId = vo.stockId;
			cpvo.checkPoint = viewName;
			cpvo.date = this.latestDate;

			checkPointDailySelectionTable.delete(vo.stockId, latestDate, viewName);
			checkPointDailySelectionTable.insert(cpvo);
		}
	}

	public void run() {
		//this.fastExtractForView("luzao_phaseII_zijinliu_top300");
		//this.fastExtractForView("luzao_phaseIII_zijinliu_top300");
		
		this.slowAnalyseForView("zijinliu_3_days_top300");
		this.slowAnalyseForView("zijinliu_3_of_5_days_top300");
		this.slowAnalyseForView("luzao_phaseIII_zijinliu_3_days_top300");
		this.slowAnalyseForView("luzao_phaseIII_zijinliu_3_of_5_days_top300");
		this.slowAnalyseForView("luzao_phaseII_zijinliu_3_days_top300");
		this.slowAnalyseForView("luzao_phaseII_zijinliu_3_of_5_days_top300");
		this.slowAnalyseForView("luzao_phaseII_ddx_2_of_5_days_bigger_05");
		this.slowAnalyseForView("luzao_phaseIII_ddx_2_of_5_days_bigger_05");
		
		this.slowAnalyseForView("luzao_phaseII_wr_all_ind_same");
		this.slowAnalyseForView("luzao_phaseIII_wr_all_ind_same");
		this.slowAnalyseForView("luzao_phaseIII_wr_midTerm_lonTerm_same");
		this.slowAnalyseForView("luzao_phaseII_wr_midTerm_lonTerm_same");
		this.slowAnalyseForView("luzao_phaseII_wr_shoTerm_midTerm_same");
	}

	public static void main(String[] args) {
		DailyViewAnalyseRunner runner = new DailyViewAnalyseRunner();
		runner.run();
		//runner.slowAnalyseForView("luzao_phaseII_ddx_2_of_5_days_bigger_05");
	}

}
