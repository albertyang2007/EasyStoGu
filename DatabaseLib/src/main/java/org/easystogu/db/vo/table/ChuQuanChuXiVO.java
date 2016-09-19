package org.easystogu.db.vo.table;

//table: event_chuquanchuxi
public class ChuQuanChuXiVO {
    public String stockId;
    public String date;
    public double rate;
    public boolean alreadyUpdatePrice = false;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("GaoSongZhuanVO: {");
        sb.append("stockId:" + stockId);
        sb.append(", date:" + date);
        sb.append(", rate:" + rate);
        sb.append(", alreadyUpdatePrice:" + alreadyUpdatePrice);
        sb.append("}");
        return sb.toString();
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isAlreadyUpdatePrice() {
        return alreadyUpdatePrice;
    }

    public void setAlreadyUpdatePrice(boolean alreadyUpdatePrice) {
        this.alreadyUpdatePrice = alreadyUpdatePrice;
    }

}
