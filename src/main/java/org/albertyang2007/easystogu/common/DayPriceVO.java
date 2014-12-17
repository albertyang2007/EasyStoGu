package org.albertyang2007.easystogu.common;

import java.util.Iterator;

public class DayPriceVO {
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
