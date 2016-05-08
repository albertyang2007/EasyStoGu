package org.easystogu.trendmode.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import net.sf.json.JSONObject;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;

public class ModeGenerator {
	private FileConfigurationService config = FileConfigurationService.getInstance();
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

		// change the real stock price data to percentage
		StockPriceVO curVO = spList.get(0);
		for (int i = 1; i < spList.size(); i++) {
			StockPriceVO vo = spList.get(i);

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
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModeGenerator ins = new ModeGenerator();
		ins.scenarios();
	}
}
