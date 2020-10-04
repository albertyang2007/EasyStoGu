package org.easystogu.indicator.runner;

import org.easystogu.indicator.runner.history.HistoryBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryQSDDCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryShenXianCountAndSaveDBRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllHistoryIndCountAndSaveDBRunner {
	@Autowired
	@Qualifier("macdRunner")
	HistoryMacdCountAndSaveDBRunner historyMacdCountAndSaveDBRunner;
	@Autowired
	@Qualifier("kdjRunner")
	HistoryKDJCountAndSaveDBRunner historyKDJCountAndSaveDBRunner;
	@Autowired
	HistoryBollCountAndSaveDBRunner historyBollCountAndSaveDBRunner;
	@Autowired
	HistoryShenXianCountAndSaveDBRunner historyShenXianCountAndSaveDBRunner;
	@Autowired
	HistoryQSDDCountAndSaveDBRunner historyQSDDCountAndSaveDBRunner;
	
	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		historyMacdCountAndSaveDBRunner.mainWork(args);
		historyKDJCountAndSaveDBRunner.mainWork(args);
		historyBollCountAndSaveDBRunner.mainWork(args);
		historyShenXianCountAndSaveDBRunner.mainWork(args);
		historyQSDDCountAndSaveDBRunner.mainWork(args);
	}
}
