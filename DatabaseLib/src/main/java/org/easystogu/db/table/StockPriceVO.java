package org.easystogu.db.table;

import java.util.Iterator;

//yahoo��ʷ���?//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
//table name = "stockprice"
public class StockPriceVO {
	public String stockId;
	public String name;
	public String date;
	public double open;
	public double high;
	public double low;
	public double close;
	public long volume;

	public StockPriceVO() {

	}

	public StockPriceVO(Iterator<String> item) {
		this.date = item.next();
		this.open = Double.parseDouble(item.next());
		this.high = Double.parseDouble(item.next());
		this.low = Double.parseDouble(item.next());
		this.close = Double.parseDouble(item.next());
		this.volume = Long.parseLong(item.next());
	}

	public boolean isValidated() {
		return this.volume > 0 ? true : false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("StockPriceVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", open:" + open);
		sb.append(", high:" + high);
		sb.append(", low:" + low);
		sb.append(", close:" + close);
		sb.append(", volume:" + volume);
		sb.append("}");
		return sb.toString();
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

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

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}
}
