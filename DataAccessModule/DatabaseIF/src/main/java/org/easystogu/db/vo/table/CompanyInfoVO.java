package org.easystogu.db.vo.table;

import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;

public class CompanyInfoVO {
  public String stockId;
  public String name;
  public String suoShuHangYe;// 所属行业
  public double totalGuBen;// 总股本
  public double liuTongAGu;// 流通股本
  public double ttmShiYingLv;// TTM市盈率
  public double shiJingLv;// 市净率
  public double liuTongBiLi;// 流通比例 = liuTongAGu / totalGuBen
  public String updateTime;

  //below fields not in table
  private double totalShiZhi;// 总市值 = totalGuBen * currentPrice
  private double liuTongShiZhi;// 流通市值 = liuTongAGu * currentPrice

  public CompanyInfoVO(String stockId, String name) {
    this.stockId = stockId;
    this.name = name;
  }

  public CompanyInfoVO() {

  }

  public CompanyInfoVO(String line) {
    // 代码, 名称,TTM市盈率,市净率,所属行业,总股本,流通股本
    // SZ000001,平安银行,10.72,1.11,银行,19405918000,19405753000
    try {
      String[] items = line.trim().split(",");
      if (items.length != 7) {
        System.out.println("Bad format for CompanyInfoVO line: " + line);
        return;
      }

      // skip the first line (header)
      if (items[0].startsWith("SZ") || items[0].startsWith("SH")) {
        this.stockId = items[0].substring(2);
      } else {
        this.stockId = items[0];
      }

      if (!Strings.isNumeric(this.stockId)) {
        System.out.println("Bad format for CompanyInfoVO stockId: " + this.stockId);
        return;
      }

      this.name = items[1];
      this.ttmShiYingLv = Strings.parseDouble(items[2]);
      this.shiJingLv = Strings.parseDouble(items[3]);
      this.suoShuHangYe = items[4];
      this.totalGuBen = Strings.parseDouble(items[5]);
      this.liuTongAGu = Strings.parseDouble(items[6]);
      this.liuTongBiLi = (this.totalGuBen != 0) ? (this.liuTongAGu / this.totalGuBen) : 0;
      this.updateTime = WeekdayUtil.currentDate();

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(line);
    }
  }

  public double convert2Double(String item) {
    if (item == null || "".equals(item) || "0".equals(item) || item.contains("-")) {
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

  public double getLiuTongShiZhi(double currentPrice) {
    this.liuTongShiZhi =  this.liuTongAGu * currentPrice;
    return this.liuTongShiZhi;
  }
  
  public double getTotalShiZhi(double currentPrice ) {
    this.totalShiZhi = totalShiZhi * currentPrice;
    return totalShiZhi;
  }

  public String getSuoShuHangYe() {
    return suoShuHangYe;
  }

  public void setSuoShuHangYe(String suoShuHangYe) {
    this.suoShuHangYe = suoShuHangYe;
  }

  public double getTtmShiYingLv() {
    return ttmShiYingLv;
  }

  public void setTtmShiYingLv(double ttmShiYingLv) {
    this.ttmShiYingLv = ttmShiYingLv;
  }

  public double getShiJingLv() {
    return shiJingLv;
  }

  public void setShiJingLv(double shiJingLv) {
    this.shiJingLv = shiJingLv;
  }

  public double getLiuTongBiLi() {
    return liuTongBiLi;
  }

  public void setLiuTongBiLi(double liuTongBiLi) {
    this.liuTongBiLi = liuTongBiLi;
  }

  @Override
  public String toString() {
    return "CompanyInfoVO [stockId=" + stockId + ", name=" + name + ", suoShuHangYe=" + suoShuHangYe
        + ", totalGuBen=" + totalGuBen + ", liuTongAGu=" + liuTongAGu + ", ttmShiYingLv="
        + ttmShiYingLv + ", shiJingLv=" + shiJingLv + ", liuTongBiLi=" + liuTongBiLi
        + ", updateTime=" + updateTime + ", totalShiZhi=" + totalShiZhi + ", liuTongShiZhi="
        + liuTongShiZhi + "]";
  }
}
