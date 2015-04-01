package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.table.BollVO;
import org.easystogu.indicator.runner.DailyBollCountAndSaveDBRunner;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.SinaDataDownloadHelper;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;

public class RealtimeDisplayStockPriceRunner {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 显示实时数据(指定的stockIds)
		StringBuffer out = new StringBuffer();
		StringBuffer alarm = new StringBuffer();
		FileConfigurationService configure = FileConfigurationService.getInstance();
		SinaDataDownloadHelper ins = new SinaDataDownloadHelper();
		DailyStockPriceDownloadAndStoreDBRunner dailyStockRunner = new DailyStockPriceDownloadAndStoreDBRunner();
		DailyBollCountAndSaveDBRunner dailyBollRunner = new DailyBollCountAndSaveDBRunner();

		String strList = configure.getString("realtime.display.stock.list") + ","
				+ configure.getString("analyse.select.stock.list");

		System.out.println("============Main Selected===========");
		List<RealTimePriceVO> list = ins.fetchDataFromWeb(strList);
		for (RealTimePriceVO vo : list) {
			dailyStockRunner.saveIntoDB(vo.convertToStockPriceVO());
			if (!vo.stockId.equals("000001")) {
				BollVO bollVO = dailyBollRunner.countAndSaved(vo.stockId);
				// System.out.println(vo);
				out.append(vo + "\t" + bollVO.toSimpleString() + "\n");
				if (vo.current >= bollVO.up) {
					alarm.append(vo.stockId + " Sell!!!\n");
				} else if (vo.current <= bollVO.dn) {
					alarm.append(vo.stockId + " Buy!!!!\n");
				}
			} else {
				out.append(vo + "\n");
			}
		}

		System.out.println(out.toString());
		System.out.println(alarm.toString());
	}
}
