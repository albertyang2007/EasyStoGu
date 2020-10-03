package org.easystogu.report;

import java.util.List;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.file.access.CompanyInfoFileHelper;

//�����ʷ���ͳ�Ƴ���������earnPercent�����VO
public class RangeHistoryReportVO {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	public List<HistoryReportDetailsVO> historyReporList;
	public String stockId;
	public DailyCombineCheckPoint checkPoint;
	public double[] avgEarnPercent = new double[3];

	public StockSuperVO currentSuperVO;

	@Override
	public String toString() {
		double priceIncreaseToday = ((this.currentSuperVO.priceVO.close - this.currentSuperVO.priceVO.lastClose) * 100.0)
				/ this.currentSuperVO.priceVO.lastClose;
		return stockId + " " + getStockName(this.currentSuperVO.priceVO.stockId) + " "
				+ this.currentSuperVO.priceVO.close + " (" + format2f(priceIncreaseToday) + ") "
				+ checkPoint.toStringWithDetails() + ", VolumeIncrease="
				+ format2f(this.currentSuperVO.volumeIncreasePercent) + ", priceHigherThanNDay="
				+ this.currentSuperVO.priceHigherThanNday + ", history size=" + historyReporList.size() + ", avgClose="
				+ format2f(avgEarnPercent[0]) + ", avgHigh=" + format2f(avgEarnPercent[1]) + ", avgLow="
				+ format2f(avgEarnPercent[2]);
	}

	public String toSimpleString() {
		double priceIncreaseToday = ((this.currentSuperVO.priceVO.close - this.currentSuperVO.priceVO.lastClose) * 100.0)
				/ this.currentSuperVO.priceVO.lastClose;

		double liuTongShiZhi = 0.0;
		CompanyInfoVO companyVO = stockConfig.getByStockId(stockId);
		if (companyVO != null) {
			liuTongShiZhi = companyVO.countLiuTongShiZhi(currentSuperVO.priceVO.close);
		}

		return stockId + " " + getStockName(this.currentSuperVO.priceVO.stockId) + " "
				+ this.currentSuperVO.priceVO.close + " (" + format2f(priceIncreaseToday) + "%) " + " ("
				+ (int) liuTongShiZhi + "亿) " + checkPoint.toStringWithDetails();
	}

	public String getStockName(String stockId) {
		return stockConfig.getStockName(stockId);
	}

	public String format2f(double d) {
		return String.format("%.2f", d);
	}
}
