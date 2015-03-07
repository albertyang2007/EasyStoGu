package org.easystogu.report.comparator;

import java.util.Comparator;

import org.easystogu.report.RangeHistoryReportVO;

@SuppressWarnings("rawtypes")
public class AvgHighEarnPercentComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		RangeHistoryReportVO vo1 = (RangeHistoryReportVO) arg0;
		RangeHistoryReportVO vo2 = (RangeHistoryReportVO) arg1;

		// �Ƚ�avgHighEarnPercent
		return (vo1.avgEarnPercent[1] >= vo2.avgEarnPercent[1]) ? 0 : 1;
	}
}
