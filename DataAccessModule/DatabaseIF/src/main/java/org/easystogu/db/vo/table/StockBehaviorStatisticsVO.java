package org.easystogu.db.vo.table;

public class StockBehaviorStatisticsVO {
    public String stockId;
    public String checkPoint;
    public double statistics;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public double getStatistics() {
        return statistics;
    }

    public void setStatistics(double statistics) {
        this.statistics = statistics;
    }
}
