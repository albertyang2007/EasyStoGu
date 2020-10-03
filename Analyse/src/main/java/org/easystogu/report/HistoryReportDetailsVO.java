package org.easystogu.report;

import java.util.List;

import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointHistorySelectionVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.StockSuperVO;

public class HistoryReportDetailsVO {
	public List<StockSuperVO> overDayList;

	public String stockId;
	public StockPriceVO buyPriceVO;
	public StockPriceVO sellPriceVO;
	public double lowPrice;
	public double highPrice;
	public String highPriceDate;
	public int duration;
	public double[] earnPercent = new double[3];// 按照收盘价买和卖;按照收盘价买和最高价卖;按照收盘价买和最低价卖
	public int holdDays;// 持股天数
	public int holdDaysWhenHighPrice;
	public boolean completed = true;

	public HistoryReportDetailsVO(StockPriceVO buy, StockPriceVO sell) {
		this.buyPriceVO = buy;
		this.sellPriceVO = sell;
	}

	public HistoryReportDetailsVO(List<StockSuperVO> overDayList) {
		this.overDayList = overDayList;
	}

	public void countData(StockPriceTableHelper stockPriceTable) {
		this.stockId = this.buyPriceVO.stockId;
		this.lowPrice = this.getLowPriceBetweenDate(this.stockId, this.buyPriceVO.date, this.sellPriceVO.date);
		this.highPrice = this.getHighPriceBetweenDate(this.stockId, this.buyPriceVO.date, this.sellPriceVO.date);
		this.highPriceDate = this.getHighPriceDateBetweenDate(this.stockId, this.highPrice, this.buyPriceVO.date,
				this.sellPriceVO.date);

		this.earnPercent[0] = ((this.sellPriceVO.close - this.buyPriceVO.close) * 100.0) / this.buyPriceVO.close;
		this.earnPercent[1] = ((this.highPrice - this.buyPriceVO.close) * 100.0) / this.buyPriceVO.close;
		this.earnPercent[2] = ((this.lowPrice - this.buyPriceVO.close) * 100.0) / this.buyPriceVO.close;

		this.holdDays = stockPriceTable.getNumberDayByIdAndBetweenDates(this.stockId, buyPriceVO.date, sellPriceVO.date);
		this.holdDaysWhenHighPrice = stockPriceTable.getNumberDayByIdAndBetweenDates(this.stockId, buyPriceVO.date,
				highPriceDate);
	}

	public Double getHighPriceBetweenDate(String stockId, String startDate, String endDate) {
		Double maxHigh = Double.MIN_VALUE;
		for (StockSuperVO superVO : overDayList) {
			if (superVO.priceVO.date.compareTo(startDate) >= 0 && superVO.priceVO.date.compareTo(endDate) <= 0) {
				if (maxHigh < superVO.priceVO.high) {
					maxHigh = superVO.priceVO.high;
				}
			}
		}
		return maxHigh;
	}

	public Double getLowPriceBetweenDate(String stockId, String startDate, String endDate) {
		Double minLow = Double.MAX_VALUE;
		for (StockSuperVO superVO : overDayList) {
			if (superVO.priceVO.date.compareTo(startDate) >= 0 && superVO.priceVO.date.compareTo(endDate) <= 0) {
				if (minLow > superVO.priceVO.low) {
					minLow = superVO.priceVO.low;
				}
			}
		}
		return minLow;
	}

	public String getHighPriceDateBetweenDate(String stockId, double high, String startDate, String endDate) {
		String date = "1970-01-01";
		for (StockSuperVO superVO : overDayList) {
			if (superVO.priceVO.date.compareTo(startDate) >= 0 && superVO.priceVO.date.compareTo(endDate) <= 0) {
				if (high == superVO.priceVO.high) {
					date = superVO.priceVO.date;
					break;
				}
			}
		}
		return date;
	}

	@Override
	public String toString() {
		return "HistoryReportVO: { stockId=" + this.buyPriceVO.stockId + "; BuyDate=" + this.buyPriceVO.date
				+ "; SellDate=" + this.sellPriceVO.date + "; minPrice=" + this.lowPrice + "; highPrice="
				+ this.highPrice + "; duration=" + this.duration + "; buyPrice=" + this.buyPriceVO.close
				+ "; closePercent=" + this.earnPercent[0] + "; highPercent=" + this.earnPercent[1] + "; lowPercent="
				+ this.earnPercent[2] + "}";
	}

	public void setBuyPriceVO(StockPriceVO buyPriceVO) {
		this.buyPriceVO = buyPriceVO;
	}

	public void setSellPriceVO(StockPriceVO sellPriceVO) {
		this.sellPriceVO = sellPriceVO;
	}

	public void setSellPriceVO(StockPriceVO selPriceVO, boolean completed) {
		this.sellPriceVO = selPriceVO;
		this.completed = completed;
	}

	public CheckPointHistorySelectionVO convertToHistoryReportVO(String checkPoint) {
		CheckPointHistorySelectionVO vo = new CheckPointHistorySelectionVO();
		vo.stockId = this.stockId;
		vo.checkPoint = checkPoint;
		vo.buyDate = this.buyPriceVO.date;
		vo.sellDate = this.sellPriceVO.date;
		return vo;
	}
}
