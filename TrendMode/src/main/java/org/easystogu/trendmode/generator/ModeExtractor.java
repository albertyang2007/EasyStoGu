package org.easystogu.trendmode.generator;

import java.util.List;

import net.sf.json.JSONArray;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;

public class ModeExtractor {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	// select range prices for one stock and return json str
	public TrendModeVO generateTrendMode(String name, String description, String stockId, String dateStart,
			String dateEnd) {
		List<StockPriceVO> spList = stockPriceTable.getStockPriceByIdAndBetweenDate(stockId, dateStart, dateEnd);
		TrendModeVO tmpVO = new TrendModeVO();
		tmpVO.setDescription(description);
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModeExtractor ins = new ModeExtractor();
		TrendModeVO rtn = ins.generateTrendMode("TestName", "Test Description", "999999", "2016-01-20", "2016-02-22");
		System.out.println(JSONArray.fromObject(rtn.prices).toString());
	}
}
