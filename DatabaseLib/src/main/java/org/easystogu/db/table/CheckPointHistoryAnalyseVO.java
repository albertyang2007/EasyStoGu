package org.easystogu.db.table;

//tableName = checkpoint_history_analyse
public class CheckPointHistoryAnalyseVO {
    public String checkPoint;
    public long totalSatisfy;
    public double closeEarnPercent;
    public double highEarnPercent;
    public double lowEarnPercent;
    public long avgHoldDays;
    public int totalHighEarn;

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public long getTotalSatisfy() {
        return totalSatisfy;
    }

    public void setTotalSatisfy(long totalSatisfy) {
        this.totalSatisfy = totalSatisfy;
    }

    public double getCloseEarnPercent() {
        return closeEarnPercent;
    }

    public void setCloseEarnPercent(double closeEarnPercent) {
        this.closeEarnPercent = closeEarnPercent;
    }

    public double getHighEarnPercent() {
        return highEarnPercent;
    }

    public void setHighEarnPercent(double highEarnPercent) {
        this.highEarnPercent = highEarnPercent;
    }

    public double getLowEarnPercent() {
        return lowEarnPercent;
    }

    public void setLowEarnPercent(double lowEarnPercent) {
        this.lowEarnPercent = lowEarnPercent;
    }

    public long getAvgHoldDays() {
        return avgHoldDays;
    }

    public void setAvgHoldDays(long avgHoldDays) {
        this.avgHoldDays = avgHoldDays;
    }

    public int getTotalHighEarn() {
        return totalHighEarn;
    }

    public void setTotalHighEarn(int totalHighEarn) {
        this.totalHighEarn = totalHighEarn;
    }
}
