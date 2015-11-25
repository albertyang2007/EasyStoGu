package org.easystogu.report;

import java.util.List;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.file.access.CompanyInfoFileHelper;

//�����ʷ���ͳ�Ƴ���������earnPercent�����VO
public class RangeHistoryReportVO {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	public List<HistoryReportDetailsVO> historyReporList;
	public String stockId;
	public DailyCombineCheckPoint checkPoint;
	public double[] avgEarnPercent = new double[3];

	public StockSuperVO currentSuperVO;

	public RangeHistoryReportVO(StockSuperVO superVO, List<HistoryReportDetailsVO> list,
			DailyCombineCheckPoint checkPoint) {
		this.currentSuperVO = superVO;
		this.stockId = superVO.priceVO.stockId;
		this.historyReporList = list;
		this.checkPoint = checkPoint;

		avgEarnPercent[0] = 0;
		avgEarnPercent[1] = 0;
		avgEarnPercent[2] = 0;

		double[] totalEarnPercent = new double[3];
		int size = this.historyReporList.size();

		for (HistoryReportDetailsVO vo : historyReporList) {
			vo.countData();
			totalEarnPercent[0] += vo.earnPercent[0];
			totalEarnPercent[1] += vo.earnPercent[1];
			totalEarnPercent[2] += vo.earnPercent[2];
		}

		if (size >= 1) {
			avgEarnPercent[0] = totalEarnPercent[0] / size;
			avgEarnPercent[1] = totalEarnPercent[1] / size;
			avgEarnPercent[2] = totalEarnPercent[2] / size;
		}

	}

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
