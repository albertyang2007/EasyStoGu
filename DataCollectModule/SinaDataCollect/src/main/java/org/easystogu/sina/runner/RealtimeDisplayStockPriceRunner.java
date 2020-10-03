package org.easystogu.sina.runner;

import java.util.List;

import org.easystogu.config.DBConfigurationService;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.DailyStockPriceDownloadHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealtimeDisplayStockPriceRunner {
	@Autowired
    private DBConfigurationService configure;

	public String printRealTimeOutput() {
		// 显示实时数据(指定的stockIds)
		StringBuffer sb = new StringBuffer();
		DailyStockPriceDownloadHelper ins = new DailyStockPriceDownloadHelper();

		String strList = configure.getString("realtime.display.stock.list", "sh000001,sz399001,sz399006")
				+ "," + configure.getString("analyse.select.stock.list");

		sb.append("============Main Selected===========<br>\n");
		// System.out.println("============Main Selected===========");
		List<RealTimePriceVO> list = ins.fetchDataFromWeb(strList);
		for (RealTimePriceVO vo : list) {
			// System.out.println(vo);
			sb.append(vo.toString() + "<br>\n");
		}
		return sb.toString();
	}
}
