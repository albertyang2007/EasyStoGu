package org.easystogu.sina.common;

/*
 * Format: list of
 * {symbol:"sh600000",code:"600000",name:"浦发银行",trade:"17.880",pricechange:"-0.360",changepercent:"-1.974",buy:"17.880",sell:"17.890",settlement:"18.240",open:"18.150",high:"18.160",low:"17.820",volume:24294360,amount:435467860,ticktime:"15:00:00",per:6.709,pb:1.169,mktcap:35139531.363636,nmc:33352406.89002,turnoverratio:0.13024}
 */
public class SinaQuoteStockPriceVO {
	public String symbol;
	public String code;
	public String name;
	public double trade;
	public double open;
	public double high;
	public double low;
	public double pricechange;
	public long volume;
	public String ticktime;

	@Override
	public String toString() {
		return this.symbol + ";" + this.code + ";" + this.name + ";" + this.open + ";" + this.trade + ";" + this.high
				+ ";" + this.low + ";" + this.volume;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTrade() {
		return trade;
	}

	public void setTrade(double trade) {
		this.trade = trade;
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

	public double getPricechange() {
		return pricechange;
	}

	public void setPricechange(double pricechange) {
		this.pricechange = pricechange;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public String getTicktime() {
		return ticktime;
	}

	public void setTicktime(String ticktime) {
		this.ticktime = ticktime;
	}
}
