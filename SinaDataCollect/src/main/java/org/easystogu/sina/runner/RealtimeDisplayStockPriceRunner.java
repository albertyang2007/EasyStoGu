package org.easystogu.sina.runner;

import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.SinaDataDownloadHelper;

public class RealtimeDisplayStockPriceRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 显示实时数据(指定的stockIds)
		FileConfigurationService configure = FileConfigurationService.getInstance();
		SinaDataDownloadHelper ins = new SinaDataDownloadHelper();

		String strList = configure.getString("realtime.display.stock.list") + ","
				+ configure.getString("analyse.select.stock.list");

		System.out.println("============Main Selected===========");
		List<RealTimePriceVO> list = ins.fetchDataFromWeb(strList);
		for (RealTimePriceVO vo : list) {
			System.out.println(vo);
		}
	}

}
