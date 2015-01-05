package org.easystogu.sina.common;

import java.util.List;

import org.easystogu.utils.Strings;

//http://hq.sinajs.cn/list=sh601318,sz000830
//http://hq.sinajs.cn/list=sh601006
//���url�᷵��һ���ı������磺
//var hq_str_sh601006="������·, 27.55, 27.25, 26.91, 27.55, 26.20, 26.91, 26.92,
//22114263, 589824680, 4695, 26.91, 57590, 26.90, 14700, 26.89, 14300,
//26.88, 15100, 26.87, 3100, 26.92, 8900, 26.93, 14230, 26.94, 25150, 26.95, 15220, 26.96, 2008-01-11, 15:05:32";
//����ַ������������ƴ����һ�𣬲�ͬ����������ö��Ÿ����ˣ����ճ���Ա��˼·��˳��Ŵ�0��ʼ��
//0����������·������Ʊ���֣�
//1����27.55�壬���տ��̼ۣ�
//2����27.25�壬�������̼ۣ�
//3����26.91�壬��ǰ�۸�
//4����27.55�壬������߼ۣ�
//5����26.20�壬������ͼۣ�
//6����26.91�壬����ۣ�������һ�����ۣ�
//7����26.92�壬�����ۣ�������һ�����ۣ�
//8����22114263�壬�ɽ��Ĺ�Ʊ�������ڹ�Ʊ������һ�ٹ�Ϊ������λ��������ʹ��ʱ��ͨ���Ѹ�ֵ����һ�٣�
//9����589824680�壬�ɽ�����λΪ��Ԫ����Ϊ��һĿ��Ȼ��ͨ���ԡ���Ԫ��Ϊ�ɽ����ĵ�λ������ͨ���Ѹ�ֵ����һ��
//10����4695�壬����һ������4695�ɣ���47�֣�
//11����26.91�壬����һ�����ۣ�
//12����57590�壬�������
//13����26.90�壬�������
//14����14700�壬��������
//15����26.89�壬��������
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
    public String codeNumber;
    public String name;
    public double open;
    public double lastClose;
    public double current;
    public double high;
    public double low;
    public double dealBuy;
    public double dealSale;
    public long volume;
    public long deal;
    public List<DealVO> dealDetails;// �嵵��ϸ
    public String date;

    public RealTimePriceVO(String codeNumber, String line) {

        if (Strings.isEmpty(line.trim())) {
            return;
        }

        this.codeNumber = codeNumber;
        int index = 0;
        String[] items = line.split(",");
        this.name = items[index++];
        this.open = Double.parseDouble(items[index++]);
        this.lastClose = Double.parseDouble(items[index++]);
        this.current = Double.parseDouble(items[index++]);
        this.high = Double.parseDouble(items[index++]);
        this.low = Double.parseDouble(items[index++]);

        this.date = items[items.length - 2];
    }

    @Override
    public String toString() {
        return this.codeNumber + ":[open:" + this.open + ", lastClose:" + this.lastClose + ",current:" + this.current
                + ",high:" + this.high + ", low:" + this.low + ",date:" + this.date + "]";
    }
}
