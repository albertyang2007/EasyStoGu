package org.easystogu.db.table;

public class CheckPointDailyStatisticsVO {
    public String date;
    public String checkPoint;
    public int count;

    @Override
    public String toString() {
        return "date: " + date + ", checkPoint=" + checkPoint + ", count=" + count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
