package org.easystogu.portal.vo;

import org.easystogu.db.vo.table.ShenXianVO;

public class ShenXianUIVO extends ShenXianVO {
	public double hc5;
	public double hc6;
	public String sellFlagsTitle = "";// 卖点
	public String sellFlagsText = "";
	public String buyFlagsTitle = "";// 买点
	public String buyFlagsText = "";
	public String duoFlagsTitle = "";// 金叉 死叉
	public String duoFlagsText = "";
	public String suoFlagsTitle = "";// 缩量
	public String suoFlagsText = "";

	public double getHc5() {
		return hc5;
	}

	public void setHc5(double hc5) {
		this.hc5 = hc5;
	}

	public double getHc6() {
		return hc6;
	}

	public void setHc6(double hc6) {
		this.hc6 = hc6;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ShenXianVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", date:" + date);
		sb.append(", h1:" + h1);
		sb.append(", h2:" + h2);
		sb.append(", h3:" + h3);
		sb.append(", hc5:" + hc5);
		sb.append(", hc6:" + hc6);
		sb.append(", sellFlagsTitle:" + sellFlagsTitle);
		sb.append(", sellFlagsText:" + sellFlagsText);
		sb.append(", buyFlagsTitle:" + buyFlagsTitle);
		sb.append(", buyFlagsText:" + buyFlagsText);
		sb.append(", duoFlagsTitle:" + duoFlagsTitle);
		sb.append(", duoFlagsText:" + duoFlagsText);
		sb.append("}");
		return sb.toString();
	}

	public String getSellFlagsTitle() {
		return sellFlagsTitle;
	}

	public void setSellFlagsTitle(String sellFlagsTitle) {
		this.sellFlagsTitle = sellFlagsTitle;
	}

	public String getSellFlagsText() {
		return sellFlagsText;
	}

	public void setSellFlagsText(String sellFlagsText) {
		this.sellFlagsText = sellFlagsText;
	}

	public String getBuyFlagsTitle() {
		return buyFlagsTitle;
	}

	public void setBuyFlagsTitle(String buyFlagsTitle) {
		this.buyFlagsTitle = buyFlagsTitle;
	}

	public String getBuyFlagsText() {
		return buyFlagsText;
	}

	public void setBuyFlagsText(String buyFlagsText) {
		this.buyFlagsText = buyFlagsText;
	}

	public String getDuoFlagsTitle() {
		return duoFlagsTitle;
	}

	public void setDuoFlagsTitle(String duoFlagsTitle) {
		this.duoFlagsTitle = duoFlagsTitle;
	}

	public String getDuoFlagsText() {
		return duoFlagsText;
	}

	public void setDuoFlagsText(String duoFlagsText) {
		this.duoFlagsText = duoFlagsText;
	}

	public String getSuoFlagsTitle() {
		return suoFlagsTitle;
	}

	public void setSuoFlagsTitle(String suoFlagsTitle) {
		this.suoFlagsTitle = suoFlagsTitle;
	}

	public String getSuoFlagsText() {
		return suoFlagsText;
	}

	public void setSuoFlagsText(String suoFlagsText) {
		this.suoFlagsText = suoFlagsText;
	}
}
