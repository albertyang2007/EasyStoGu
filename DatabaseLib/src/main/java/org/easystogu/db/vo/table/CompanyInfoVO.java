package org.easystogu.db.vo.table;

import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;

public class CompanyInfoVO {
    public String stockId;
    public String name;
    public String updateTime;
    public double totalGuBen;
    public double liuTongAGu;
    private double liuTongShiZhi;// =liuTongAGu * currentPrice

    public CompanyInfoVO(String stockId, String name) {
        this.stockId = stockId;
        this.name = name;
    }

    public CompanyInfoVO() {

    }

    public CompanyInfoVO(String line) {
        // 1074,000002,万科Ａ,111亿,97.2亿,
        try {
            String[] items = line.trim().split(",");
            if (items.length < 1) {
                System.out.println("Bad format for CompanyInfoVO line: " + line);
                return;
            }

            // skip the first line (header)
            if (!Strings.isNumeric(items[0]) || !Strings.isNumeric(items[1])) {
                return;
            }

            this.stockId = items[1];
            this.name = items[2];
            this.totalGuBen = convert2Double(items[3]);
            this.liuTongAGu = convert2Double(items[4]);
            this.updateTime = WeekdayUtil.currentDate();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(line);
        }
    }

    public double convert2Double(String item) {
        if (item == null || "".equals(item) || "0".equals(item)) {
            return 0;
        }
        // item is like: 1.2亿 or 5600万, 返回单位为亿
        if (item.contains("亿")) {
            return Double.parseDouble(item.substring(0, item.length() - 1));
        } else if (item.contains("万")) {
            return Double.parseDouble(item.substring(0, item.length() - 1)) / 10000;
        } else {
            return Double.parseDouble(item.substring(0, item.length() - 1)) / (10000 * 10000);
        }
    }

    public int countLiuTongShiZhi(double close) {
        return (int) (this.liuTongAGu * close);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("CompanyInfoVO: {");
        sb.append("stockId:" + stockId);
        sb.append(", name:" + name);
        sb.append(", updateTime:" + updateTime);
        sb.append(", totalGuBen:" + totalGuBen);
        sb.append(", liuTongAGu:" + liuTongAGu);
        sb.append(", liuTongShiZhi:" + liuTongShiZhi);
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public double getTotalGuBen() {
        return totalGuBen;
    }

    public void setTotalGuBen(double totalGuBen) {
        this.totalGuBen = totalGuBen;
    }

    public double getLiuTongAGu() {
        return liuTongAGu;
    }

    public void setLiuTongAGu(double liuTongAGu) {
        this.liuTongAGu = liuTongAGu;
    }

    public double getLiuTongShiZhi() {
        return liuTongShiZhi;
    }

    public void setLiuTongShiZhi(double liuTongShiZhi) {
        this.liuTongShiZhi = liuTongShiZhi;
    }
}
