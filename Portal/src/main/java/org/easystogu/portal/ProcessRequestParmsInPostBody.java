package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.util.MergeNDaysPriceUtil;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.portal.init.TrendModeLoader;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.config.Constants;
import org.easystogu.cache.ConfigurationServiceCache;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//process request parms in post request body, it only apply for IndicatorEndPointV3 and PriceEndPoint
//parms such as: append trendMode, merge nDays into one
//the parms has priority: nDays > trendMode

@Component
public class ProcessRequestParmsInPostBody {
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
	protected MergeNDaysPriceUtil mergeNdaysPriceHeloer = new MergeNDaysPriceUtil();
	protected StockIndicatorCache indicatorCache = StockIndicatorCache.getInstance();
	@Autowired
	protected TrendModeLoader trendModeLoader;

	public List<StockPriceVO> updateStockPriceAccordingToRequest(String stockId, String postBody) {
		List<StockPriceVO> spList = fetchAllPrices(stockId);
		if (Strings.isEmpty(postBody))
			return spList;

		try {
			JSONObject jsonParm = new JSONObject(postBody);
			// parms has process priority, do not change the order
			int repeatTimes = 1;
			String repeatTimesParms = jsonParm.getString("repeatTimes");
			if (Strings.isNotEmpty(repeatTimesParms) && Strings.isNumeric(repeatTimesParms)) {
				repeatTimes = Integer.parseInt(repeatTimesParms);
			}

			String trendModeName = jsonParm.getString("trendModeName");
			if (Strings.isNotEmpty(trendModeName)) {
				spList = this.appendTrendModePrice(trendModeName, repeatTimes, spList);
			}

			String nDays = jsonParm.getString("nDays");
			if (Strings.isNotEmpty(nDays) && Strings.isNumeric(nDays)) {
				spList = this.mergeNDaysPrice(Integer.parseInt(nDays), spList);
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

	private List<StockPriceVO> appendTrendModePrice(String trendModeName, int repeatTimes, List<StockPriceVO> spList) {
		// parse the forecast body and add back to spList
		StockPriceVO curSPVO = spList.get(spList.size() - 1);
		TrendModeVO tmo = trendModeLoader.loadTrendMode(trendModeName).copy();

		if (tmo.prices.size() == 0)
			return spList;

		List<SimplePriceVO> origList = tmo.getPricesByCopy();
		for (int i = 1; i < repeatTimes; i++) {
			tmo.prices.addAll(origList);
		}

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
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		List<Object> tmpList = this.indicatorCache.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" + stockid);
		for (Object obj : tmpList) {
			spList.add((StockPriceVO) obj);
		}
		return spList;
	}

	private String appendTrendModeDateToDateRange(String postBody, String date) {
		String fromDate = WeekdayUtil.currentDate();
		String endDate = WeekdayUtil.currentDate();

		if (Pattern.matches(Constants.fromToRegex, date)) {
			fromDate = date.split("_")[0];
			endDate = date.split("_")[1];

			// if postBody contains the trendMode, then get the dateLengh from
			// it
			// and append the last date to dateRange
			if (Strings.isNotEmpty(postBody)) {
				try {
					JSONObject jsonParm = new JSONObject(postBody);
					
					int repeatTimes = 1;
					String repeatTimesParms = jsonParm.getString("repeatTimes");
					if (Strings.isNotEmpty(repeatTimesParms) && Strings.isNumeric(repeatTimesParms)) {
						repeatTimes = Integer.parseInt(repeatTimesParms);
					}

					String trendModeName = jsonParm.getString("trendModeName");
					if (Strings.isNotEmpty(trendModeName)) {
						TrendModeVO tmo = trendModeLoader.loadTrendMode(trendModeName).copy();

						List<SimplePriceVO> origList = tmo.getPricesByCopy();
						for (int i = 1; i < repeatTimes; i++) {
							tmo.prices.addAll(origList);
						}
						
						if (tmo.prices.size() > 0) {
							String newEndDate = WeekdayUtil.nextNWorkingDate(endDate, tmo.prices.size());
							return fromDate + "_" + newEndDate;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// return the default data range
		return date;
	}

	public boolean isStockDateSelected(String postBody, String date, String aDate) {

		String newDate = this.appendTrendModeDateToDateRange(postBody, date);

		if (Pattern.matches(Constants.fromToRegex, newDate)) {
			String date1 = newDate.split("_")[0];
			String date2 = newDate.split("_")[1];
			return Strings.isDateSelected(date1 + " " + Constants.HHmmss, date2 + " " + Constants.HHmmss,
					aDate + " " + Constants.HHmmss);
		}
		if (Pattern.matches(Constants.dateRegex, newDate) || Strings.isEmpty(newDate)) {
			return aDate.equals(newDate);
		}
		return false;
	}
}
