package org.easystogu.sina.common;

import java.util.List;

import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.utils.Strings;

//http://hq.sinajs.cn/list=sh601318,sz000830
//http://hq.sinajs.cn/list=sh601006

public class RealTimePriceVO {
    public String stockId;
    public String name;
    public double open;
    public double lastClose;
    public double current;
    public double high;
    public double low;
    public double dealBuy;
    public double dealSale;
    public long volume = 0;
    public long deal;
    public List<DealVO> dealDetails;// �嵵��ϸ
    public String date;
    public String time;

    public RealTimePriceVO(String stockId, String line) {

        if (Strings.isEmpty(line.trim())) {
            return;
        }

        this.stockId = stockId;
        int index = 0;
        String[] items = line.split(",");
        this.name = items[index++];
        this.open = Strings.convert2ScaleDecimal(Double.parseDouble(items[index++]));
        this.lastClose = Strings.convert2ScaleDecimal(Double.parseDouble(items[index++]));
        this.current = Strings.convert2ScaleDecimal(Double.parseDouble(items[index++]));
        this.high = Strings.convert2ScaleDecimal(Double.parseDouble(items[index++]));
        this.low = Strings.convert2ScaleDecimal(Double.parseDouble(items[index++]));
        this.volume = Long.parseLong(items[index++ + 2]);

        this.date = items[items.length - 3];
        this.time = items[items.length - 2];
    }

    public boolean isValidated() {
        // check date format is like: 2015-09-01
        if (Strings.isDateValidate(date)) {
            return this.volume > 0 ? true : false;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.stockId + "\t[lastClose:"
                + Strings.convert2ScaleDecimalStr(this.lastClose, 2) + "\t:open:"
                + Strings.convert2ScaleDecimalStr(this.open, 2) + "(" + toPercent(this.open, this.lastClose)
                + ")" + ",\thigh:" + Strings.convert2ScaleDecimalStr(this.high, 2) + "("
                + toPercent(this.high, this.lastClose) + ")" + ",\tcur:"
                + Strings.convert2ScaleDecimalStr(this.current, 2) + "("
                + toPercent(this.current, this.lastClose) + ")" + ",\tlow:"
                + Strings.convert2ScaleDecimalStr(this.low, 2) + "(" + toPercent(this.low, this.lastClose)
                + ")" + ",\trange:" + this.diffRange(this.high, this.low, this.lastClose) + "]";
    }

    public String toPercent(double d1, double d2) {
        double p = ((d1 - d2) * 100) / d2;
        return String.format("%.2f", p) + "%";
    }

    public String diffRange(double d1, double d2, double d3) {
        double r = (((d1 - d3) * 100) / d3) - (((d2 - d3) * 100) / d3);
        return String.format("%.2f", Math.abs(r)) + "%";
    }

    public StockPriceVO convertToStockPriceVO() {
        StockPriceVO vo = new StockPriceVO();
        vo.setStockId(stockId);
        vo.setOpen(open);
        vo.setHigh(high);
        vo.setClose(current);
        vo.setLow(low);
        vo.setDate(date);
        vo.setVolume(volume);
        vo.lastClose = this.lastClose;
        return vo;
    }
}
