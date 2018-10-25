package org.easystogu.trendmode.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;

import net.sf.json.JSONObject;

public class ModeGenerator {
	private ConfigurationService config = DBConfigurationService.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	private String trendModeJsonFilePath = config.getString("trendmode.json.file.path");

	// select range prices for one stock and return json str
	public TrendModeVO generateTrendMode(String name, String description, String stockId, String dateStart,
			String dateEnd) {
		List<StockPriceVO> spList = stockPriceTable.getStockPriceByIdAndBetweenDate(stockId, dateStart, dateEnd);
		TrendModeVO tmpVO = new TrendModeVO();
		tmpVO.setDescription("Select from " + stockConfig.getStockName(stockId) + "(" + stockId + "); Time: ("
				+ dateStart + " ~ " + dateEnd + ") " + description + ".");
		tmpVO.setLength(spList.size() - 1);
		tmpVO.setName(name);

		double high = 0;
		double low = 0;

		// change the real stock price data to percentage
		if (spList.size() > 0) {
			StockPriceVO curVO = spList.get(0);
			high = curVO.high;
			low = curVO.low;

			for (int i = 1; i < spList.size(); i++) {
				StockPriceVO vo = spList.get(i);

				if (vo.high > high) {
					high = vo.high;
				}
				if (vo.low < low) {
					low = vo.low;
				}

				SimplePriceVO spvo = new SimplePriceVO();
				spvo.close = Strings.convert2ScaleDecimal(100.0 * (vo.close - curVO.close) / curVO.close);
				spvo.open = Strings.convert2ScaleDecimal(100.0 * (vo.open - curVO.close) / curVO.close);
				spvo.high = Strings.convert2ScaleDecimal(100.0 * (vo.high - curVO.close) / curVO.close);
				spvo.low = Strings.convert2ScaleDecimal(100.0 * (vo.low - curVO.close) / curVO.close);
				spvo.volume = Strings.convert2ScaleDecimal(vo.volume * 1.0 / curVO.volume);
				spvo.date = vo.date;
				spvo.stockId = vo.stockId;
				// set next vo to curVO
				curVO = vo;
				tmpVO.getPrices().add(spvo);
			}
		}

		// count the high and low percent
		if (low > 0) {
			tmpVO.zhengfu = (high - low) / low;
		}

		return tmpVO;
	}

	private void saveToFile(TrendModeVO tmo) {
		String file = trendModeJsonFilePath + tmo.name + ".json";
		System.out.println("Saving TrendMode to " + file);
		try {
			BufferedWriter fout = new BufferedWriter(new FileWriter(file));
			fout.write(JSONObject.fromObject(tmo).toString());
			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void scenarios() {
		/*
		saveToFile(generateTrendMode("Platform_8", "暴涨,长平台整理,突破,下跌", "000701", "2015-09-30", "2016-01-04"));
		saveToFile(generateTrendMode("M_Tou", "M 头", "999999", "2015-11-03", "2016-01-04"));
		saveToFile(generateTrendMode("BaoDie", "暴跌", "999999", "2015-08-17", "2015-10-08"));
		saveToFile(generateTrendMode("BaoZhang", "暴涨", "999999", "2015-05-18", "2015-06-15"));
		saveToFile(generateTrendMode("BaoZhang2", "西部证券暴涨", "002673", "2015-09-15", "2015-11-17"));
		saveToFile(generateTrendMode("MidFanTan", "中级反弹", "999999", "2008-11-10", "2009-02-17"));
		saveToFile(generateTrendMode("GuoQingHangQing", "国庆行情", "999999", "2015-09-28", "2015-11-09"));
		saveToFile(generateTrendMode("Break_Platform_1", "长平台整理,阶梯上升", "600021", "2015-01-20", "2015-04-17"));
		saveToFile(generateTrendMode("Break_Platform_2", "短平台整理,阶梯上升", "000979", "2015-02-25", "2015-04-14"));
		saveToFile(generateTrendMode("Break_Platform_3", "短平台整理,阶梯上升", "300216", "2015-03-06", "2015-04-28"));
		saveToFile(generateTrendMode("ZiMaKaiHua", "芝麻开花,节节高", "600408", "2015-02-06", "2015-03-24"));
		saveToFile(generateTrendMode("ZiMaKaiHua2", "芝麻开花,节节高", "603866", "2016-04-22", "2016-05-11"));
		saveToFile(generateTrendMode("SuoLiangHuiTiao", "缩量回调，20日均线支撑", "603866", "2016-03-24", "2016-05-23"));
		saveToFile(generateTrendMode("None", "None", "999999", "2016-01-01", "2016-01-01"));
		
		//new added
		saveToFile(generateTrendMode("Wuliang3LianBan", "无量3个一字板", "002809", "2016-08-24", "2016-08-29"));
		saveToFile(generateTrendMode("Wuliang5LianBan", "无量5个一字板", "002809", "2016-08-24", "2016-08-31"));
		saveToFile(generateTrendMode("Wuliang7LianBan", "无量7个一字板", "002809", "2016-08-24", "2016-09-02"));
		saveToFile(generateTrendMode("Fangliang3LianBan", "放量3连板", "603999", "2016-09-30", "2016-10-12"));
		saveToFile(generateTrendMode("HengPan4Zhou", "横盘四周突破", "000049", "2017-02-07", "2017-03-10"));
		saveToFile(generateTrendMode("HengPan1Zhou", "横盘一周突破", "000423", "2017-03-10", "2017-03-17"));
		saveToFile(generateTrendMode("HengPan2Zhou", "横盘两周突破", "002673", "2015-03-20", "2015-04-08"));
		*/
		
		saveToFile(generateTrendMode("LuZaoPhaseII", "鲁兆持股阶段", "600547", "2018-09-20", "2018-10-25"));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModeGenerator ins = new ModeGenerator();
		ins.scenarios();
	}
}
