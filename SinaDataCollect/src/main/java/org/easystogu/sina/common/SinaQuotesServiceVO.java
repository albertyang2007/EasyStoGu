package org.easystogu.sina.common;

public class SinaQuotesServiceVO {
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
