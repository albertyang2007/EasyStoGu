package org.easystogu.indicator.runner.history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndicatorHistortOverAllRunner {
	@Autowired
	HistoryMacdCountAndSaveDBRunner historyMacdCountAndSaveDBRunner;
	@Autowired
	HistoryKDJCountAndSaveDBRunner historyKDJCountAndSaveDBRunner;
	@Autowired
	HistoryBollCountAndSaveDBRunner historyBollCountAndSaveDBRunner;
	@Autowired
	HistoryShenXianCountAndSaveDBRunner historyShenXianCountAndSaveDBRunner;
	@Autowired
	HistoryQSDDCountAndSaveDBRunner historyQSDDCountAndSaveDBRunner;
	@Autowired
	HistoryWRCountAndSaveDBRunner historyWRCountAndSaveDBRunner;
	@Autowired
	HistoryMACountAndSaveDBRunner historyMACountAndSaveDBRunner;
	@Autowired
	HistoryWeeklyMacdCountAndSaveDBRunner historyWeeklyMacdCountAndSaveDBRunner;
	@Autowired
	HistoryWeeklyKDJCountAndSaveDBRunner historyWeeklyKDJCountAndSaveDBRunner;
	
	public void countAndSave(List<String> stockIds) {
		// day
		historyMacdCountAndSaveDBRunner.countAndSaved(stockIds);
		historyKDJCountAndSaveDBRunner.countAndSaved(stockIds);
		historyBollCountAndSaveDBRunner.countAndSaved(stockIds);
		historyShenXianCountAndSaveDBRunner.countAndSaved(stockIds);
		historyQSDDCountAndSaveDBRunner.countAndSaved(stockIds);
		historyWRCountAndSaveDBRunner.countAndSaved(stockIds);
		historyMACountAndSaveDBRunner.countAndSaved(stockIds);
		// week
		historyWeeklyMacdCountAndSaveDBRunner.countAndSaved(stockIds);
		historyWeeklyKDJCountAndSaveDBRunner.countAndSaved(stockIds);
	}

	public void countAndSave(String stockId) {
		// day
		historyMacdCountAndSaveDBRunner.countAndSaved(stockId);
		historyKDJCountAndSaveDBRunner.countAndSaved(stockId);
		historyBollCountAndSaveDBRunner.countAndSaved(stockId);
		historyShenXianCountAndSaveDBRunner.countAndSaved(stockId);
		historyQSDDCountAndSaveDBRunner.countAndSaved(stockId);
		historyWRCountAndSaveDBRunner.countAndSaved(stockId);
		// week
		historyWeeklyMacdCountAndSaveDBRunner.countAndSaved(stockId);
		historyWeeklyKDJCountAndSaveDBRunner.countAndSaved(stockId);
	}

	public void run() {
		String[] args = null;
		// day
		historyMacdCountAndSaveDBRunner.mainWork(args);
		historyKDJCountAndSaveDBRunner.mainWork(args);
		historyBollCountAndSaveDBRunner.mainWork(args);
		historyShenXianCountAndSaveDBRunner.mainWork(args);
		historyQSDDCountAndSaveDBRunner.mainWork(args);
		historyWRCountAndSaveDBRunner.mainWork(args);
		// week
		historyWeeklyMacdCountAndSaveDBRunner.mainWork(args);
		historyWeeklyKDJCountAndSaveDBRunner.mainWork(args);

	}

	public void mainWork(String[] args) {
		this.run();
	}
}
