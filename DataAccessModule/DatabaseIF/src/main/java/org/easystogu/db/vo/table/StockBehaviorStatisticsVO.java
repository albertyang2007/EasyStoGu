package org.easystogu.db.vo.table;

public class StockBehaviorStatisticsVO {
    public String stockId;
    public String checkPoint;
    public String statistics;

    @Override
    public String toString() {
        return stockId + "," + checkPoint + "," + statistics;
    }

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

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }
}
