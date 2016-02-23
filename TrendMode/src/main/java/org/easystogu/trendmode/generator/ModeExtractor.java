package org.easystogu.trendmode.generator;

import java.util.List;

import net.sf.json.JSONArray;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;

public class ModeExtractor {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();

	// select range prices for one stock and return json str
	public TrendModeVO generateTrendMode(String name, String description, String stockId, String dateStart,
			String dateEnd) {
		List<StockPriceVO> spList = stockPriceTable.getStockPriceByIdAndBetweenDate(stockId, dateStart, dateEnd);
		TrendModeVO tmpVO = new TrendModeVO();
		tmpVO.setDescription("Select from " + stockConfig.getStockName(stockId) + "(" + stockId + "); Time: ("
				+ dateStart + " ~ " + dateEnd + ") " + description);
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
			// set next vo to curVO
			curVO = vo;
			tmpVO.getPrices().add(spvo);
		}

		return tmpVO;
	}

	public void scenarios() {
		TrendModeVO tmo = null;
		tmo = this.generateTrendMode("Platform_8", "暴涨,长平台整理,突破,下跌", "000701", "2015-09-30", "2016-01-04");
		System.out.println(JSONArray.fromObject(tmo).toString());

		tmo = this.generateTrendMode("M_Tou", "M 头", "999999", "2015-11-03", "2016-01-04");
		System.out.println(JSONArray.fromObject(tmo).toString());
		
		tmo = this.generateTrendMode("Break_Platform_1", "长平台整理,阶梯上升", "600021", "2015-01-20", "2015-04-17");
		System.out.println(JSONArray.fromObject(tmo).toString());
		
		tmo = this.generateTrendMode("Break_Platform_2", "短平台整理,阶梯上升", "000979", "2015-02-25", "2015-04-14");
		System.out.println(JSONArray.fromObject(tmo).toString());
		
		tmo = this.generateTrendMode("ZiMaKaiHua", "芝麻开花,节节高", "600408", "2015-02-06", "2015-03-24");
		System.out.println(JSONArray.fromObject(tmo).toString());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModeExtractor ins = new ModeExtractor();
		ins.scenarios();
	}
}
