package org.easystogu.sina.common;

import java.util.List;
import java.util.regex.Pattern;

import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.utils.Strings;

//http://hq.sinajs.cn/list=sh601318,sz000830
//http://hq.sinajs.cn/list=sh601006
//���url�᷵��һ���ı������磺
//var hq_str_sh601006="������·, 27.55, 27.25, 26.91, 27.55, 26.20, 26.91, 26.92,
//22114263, 589824680, 4695, 26.91, 57590, 26.90, 14700, 26.89, 14300,
//26.88, 15100, 26.87, 3100, 26.92, 8900, 26.93, 14230, 26.94, 25150, 26.95, 15220, 26.96, 2008-01-11, 15:05:32";
//����ַ���������ƴ����һ�𣬲�ͬ���������ö��Ÿ����ˣ����ճ���Ա��˼·��˳��Ŵ�0��ʼ��
//0����������·������Ʊ���֣�
//1����27.55�壬���տ��̼ۣ�
//2����27.25�壬�������̼ۣ�
//3����26.91�壬��ǰ�۸�
//4����27.55�壬������߼ۣ�
//5����26.20�壬������ͼۣ�
//6����26.91�壬����ۣ�������һ�����ۣ�
//7����26.92�壬�����ۣ�������һ�����ۣ�
//8����22114263�壬�ɽ��Ĺ�Ʊ�����ڹ�Ʊ������һ�ٹ�Ϊ��λ��������ʹ��ʱ��ͨ���Ѹ�ֵ����һ�٣�
//9����589824680�壬�ɽ�����λΪ��Ԫ����Ϊ��һĿ��Ȼ��ͨ���ԡ���Ԫ��Ϊ�ɽ����ĵ�λ������ͨ���Ѹ�ֵ����һ��
//10����4695�壬����һ������4695�ɣ���47�֣�
//11����26.91�壬����һ�����ۣ�
//12����57590�壬�������
//13����26.90�壬�������
//14����14700�壬������
//15����26.89�壬������
//16����14300�壬�����ġ�
//17����26.88�壬�����ġ�
//18����15100�壬�����塱
//19����26.87�壬�����塱
//20����3100�壬����һ���걨3100�ɣ���31�֣�
//21����26.92�壬����һ������
//(22, 23), (24, 25), (26,27), (28, 29)�ֱ�Ϊ���������������ĵ������
//30����2008-01-11�壬���ڣ�
//31����15:05:32�壬ʱ�䣻

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
                + Strings.convert2ScaleDecimalAlignWithLength(this.lastClose, 2) + "\t:open:"
                + Strings.convert2ScaleDecimalAlignWithLength(this.open, 2) + "(" + toPercent(this.open, this.lastClose)
                + ")" + ",\thigh:" + Strings.convert2ScaleDecimalAlignWithLength(this.high, 2) + "("
                + toPercent(this.high, this.lastClose) + ")" + ",\tcur:"
                + Strings.convert2ScaleDecimalAlignWithLength(this.current, 2) + "("
                + toPercent(this.current, this.lastClose) + ")" + ",\tlow:"
                + Strings.convert2ScaleDecimalAlignWithLength(this.low, 2) + "(" + toPercent(this.low, this.lastClose)
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
