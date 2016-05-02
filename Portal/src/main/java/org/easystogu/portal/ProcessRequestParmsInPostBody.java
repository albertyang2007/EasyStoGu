package org.easystogu.portal;

import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.util.MergeNDaysPriceUtil;
import org.easystogu.portal.init.TrendModeLoader;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//process request parms in post request body, it only apply for IndicatorEndPointV3 and PriceEndPoint
//parms such as: append trendMode, merge nDays into one
//the parms has priority: nDays > trendMode

@Component
public class ProcessRequestParmsInPostBody {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected MergeNDaysPriceUtil mergeNdaysPriceHeloer = new MergeNDaysPriceUtil();
	@Autowired
	protected TrendModeLoader trendModeLoader;

	public List<StockPriceVO> updateStockPriceAccordingToRequest(String stockId, String postBody) {
		List<StockPriceVO> spList = fetchAllPrices(stockId);
		if (Strings.isEmpty(postBody))
			return spList;

		try {
			JSONObject jsonParm = new JSONObject(postBody);
			// parms has process priority, do not change the order
			String nDays = jsonParm.getString("nDays");
			if (Strings.isNotEmpty(nDays) && Strings.isNumeric(nDays)) {
				spList = this.mergeNDaysPrice(Integer.parseInt(nDays), spList);
			}

			String trendModeName = jsonParm.getString("trendModeName");
			if (Strings.isNotEmpty(trendModeName)) {
				spList = this.appendTrendModePrice(trendModeName, spList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// finally return the updated spList
		return spList;
	}

	private List<StockPriceVO> mergeNDaysPrice(int nDays, List<StockPriceVO> spList) {
		return mergeNdaysPriceHeloer.generateNDaysPriceVOInDescOrder(nDays, spList);
	}

	private List<StockPriceVO> appendTrendModePrice(String trendModeName, List<StockPriceVO> spList) {
		// parse the forecast body and add back to spList
		StockPriceVO curSPVO = spList.get(spList.size() - 1);
		TrendModeVO tmo = trendModeLoader.loadTrendMode(trendModeName);
		List<String> nextWorkingDateList = WeekdayUtil.nextWorkingDateList(curSPVO.date, tmo.prices.size());

		for (int i = 0; i < tmo.prices.size(); i++) {
			SimplePriceVO svo = tmo.prices.get(i);
			StockPriceVO spvo = new StockPriceVO();
			spvo.setDate(nextWorkingDateList.get(i));
			spvo.setStockId(curSPVO.stockId);
			spvo.setLastClose(curSPVO.close);
			spvo.setOpen(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getOpen() / 100.0)));
			spvo.setClose(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getClose() / 100.0)));
			spvo.setLow(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getLow() / 100.0)));
			spvo.setHigh(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getHigh() / 100.0)));
			spvo.setVolume((long) (curSPVO.volume * svo.getVolume()));

			spList.add(spvo);
			curSPVO = spvo;
		}

		return spList;
	}

	// common function to fetch price from stockPrice table
	private List<StockPriceVO> fetchAllPrices(String stockid) {
		List<StockPriceVO> spList = null;
		spList = stockPriceTable.getStockPriceById(stockid);
		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updateQianFuQianPriceBasedOnHouFuQuan(stockid, spList);
		return spList;
	}

}
