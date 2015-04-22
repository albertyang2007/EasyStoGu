package org.easystogu.report;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.CheckPointHistorySelectionVO;
import org.easystogu.db.table.StockPriceVO;

public class HistoryReportDetailsVO {

    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();

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

    public HistoryReportDetailsVO() {
    }

    public void countData() {
        this.stockId = this.buyPriceVO.stockId;
        this.lowPrice = stockPriceTable.getLowPriceBetweenDate(this.stockId, this.buyPriceVO.date,
                this.sellPriceVO.date);
        this.highPrice = stockPriceTable.getHighPriceBetweenDate(this.stockId, this.buyPriceVO.date,
                this.sellPriceVO.date);
        this.highPriceDate = stockPriceTable.getHighPriceDateBetweenDate(this.stockId, this.highPrice,
                this.buyPriceVO.date, this.sellPriceVO.date);

        this.earnPercent[0] = ((this.sellPriceVO.close - this.buyPriceVO.close) * 100.0) / this.buyPriceVO.close;
        this.earnPercent[1] = ((this.highPrice - this.buyPriceVO.close) * 100.0) / this.buyPriceVO.close;
        this.earnPercent[2] = ((this.lowPrice - this.buyPriceVO.close) * 100.0) / this.buyPriceVO.close;

        this.holdDays = stockPriceTable.getDaysByIdAndBetweenDates(this.stockId, buyPriceVO.date, sellPriceVO.date);
        this.holdDaysWhenHighPrice = stockPriceTable.getDaysByIdAndBetweenDates(this.stockId, buyPriceVO.date,
                highPriceDate);
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

    public void setSelPriceVO(StockPriceVO selPriceVO, boolean completed) {
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
