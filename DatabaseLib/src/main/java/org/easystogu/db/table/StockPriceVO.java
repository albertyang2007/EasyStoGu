package org.easystogu.db.table;

import java.util.Iterator;

//yahoo锟斤拷史锟斤拷锟�//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
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
	public double lastClose;

	public StockPriceVO() {

	}

	public StockPriceVO(String line) {
		// 日期 开盘价 最高价 收盘价 最低价 交易量(股) 交易金额(元)
		try {
			String[] items = line.split(" ");
			this.date = items[0];
			this.open = Double.parseDouble(items[1]);
			this.high = Double.parseDouble(items[2]);
			this.close = Double.parseDouble(items[3]);
			this.low = Double.parseDouble(items[4]);
			this.volume = Long.parseLong(items[5]);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public StockPriceVO copy() {
		StockPriceVO copy = new StockPriceVO();
		copy.close = this.close;
		copy.date = this.date;
		copy.high = this.high;
		copy.low = this.low;
		copy.name = this.name;
		copy.open = this.open;
		copy.stockId = this.stockId;
		copy.volume = this.volume;
		copy.lastClose = this.lastClose;
		return copy;
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
		sb.append(", lastClose:" + lastClose);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((date == null) ? 0 : date.hashCode());
		result = (prime * result) + ((stockId == null) ? 0 : stockId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StockPriceVO other = (StockPriceVO) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (stockId == null) {
			if (other.stockId != null) {
				return false;
			}
		} else if (!stockId.equals(other.stockId)) {
			return false;
		}
		return true;
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

	public double getLastClose() {
		return lastClose;
	}

	public void setLastClose(double lastClose) {
		this.lastClose = lastClose;
	}
}
