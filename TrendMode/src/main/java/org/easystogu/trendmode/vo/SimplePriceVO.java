package org.easystogu.trendmode.vo;

public class SimplePriceVO {

    public String stockId;

    public double open;

    public double high;

    public double low;

    public double close;

    public double volume;

    public String date;

    public double getOpen() {

        return open;

    }

    public void setOpen(double open) {

        this.open = open;

    }

    public double getHigh() {

        return high;

    }

    public void setHigh(double high) {

        this.high = high;

    }

    public double getLow() {

        return low;

    }

    public void setLow(double low) {

        this.low = low;

    }

    public double getClose() {

        return close;

    }

    public void setClose(double close) {

        this.close = close;

    }

    public double getVolume() {

        return volume;

    }

    public void setVolume(double volume) {

        this.volume = volume;

    }

    public String getDate() {

        return date;

    }

    public void setDate(String date) {

        this.date = date;

    }

    public String getStockId() {

        return stockId;

    }

    public void setStockId(String stockId) {

        this.stockId = stockId;

    }

}
