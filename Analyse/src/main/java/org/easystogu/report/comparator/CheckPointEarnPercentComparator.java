package org.easystogu.report.comparator;

import java.util.Comparator;

import org.easystogu.report.RangeHistoryReportVO;

public class CheckPointEarnPercentComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
        RangeHistoryReportVO vo1 = (RangeHistoryReportVO) arg0;
        RangeHistoryReportVO vo2 = (RangeHistoryReportVO) arg1;

        // �Ƚ�avgHighEarnPercent
        return (vo1.checkPoint.getEarnPercent() >= vo2.checkPoint.getEarnPercent()) ? 0 : 1;
    }

}
