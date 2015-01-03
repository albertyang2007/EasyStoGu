package org.easystogu.yahoo.common;

import java.util.Iterator;

//yahoo历史数据
//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
public class DayPriceVO {
	public String codeNumber;
	public String name;
	public String date;
	public double open;
	public double high;
	public double low;
	public double close;
	public long volume;
	public double adjClose;

	public DayPriceVO(Iterator<String> item) {
		this.date = item.next();
		this.open = Double.parseDouble(item.next());
		this.high = Double.parseDouble(item.next());
		this.low = Double.parseDouble(item.next());
		this.close = Double.parseDouble(item.next());
		this.volume = Long.parseLong(item.next());
		this.adjClose = Double.parseDouble(item.next());
	}

	public boolean isValidated() {
		return this.volume > 0 ? true : false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("DayPriceVO: {");
		sb.append("codeNumber:" + codeNumber);
		sb.append("name:" + name);
		sb.append("date:" + date);
		sb.append(", open:" + open);
		sb.append(", high:" + high);
		sb.append(", low:" + low);
		sb.append(", close:" + close);
		sb.append(", volume:" + volume);
		sb.append(", adjClose:" + adjClose);
		sb.append("}");
		return sb.toString();
	}
}
