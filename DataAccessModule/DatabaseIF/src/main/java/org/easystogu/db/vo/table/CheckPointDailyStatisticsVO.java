package org.easystogu.db.vo.table;

public class CheckPointDailyStatisticsVO {
    public String date;
    public String checkPoint;
    public int count;
    public double rate;

    @Override
    public String toString() {
        return "date: " + date + ", checkPoint=" + checkPoint + ", count=" + count + ", rate=" + rate;
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

    public double getRate() {
      return rate;
    }

    public void setRate(double rate) {
      this.rate = rate;
    }
}
