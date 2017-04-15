package org.easystogu.trendmode.vo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

public class TrendModeVO {
	public String name;
	public String description;
	public int length;
	public double zhengfu;//振幅
	public double zhangdie;//最终涨跌点数
	public List<SimplePriceVO> prices = new ArrayList<SimplePriceVO>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<SimplePriceVO> getPrices() {
		return prices;
	}

	public void setPrices(List<SimplePriceVO> prices) {
		this.prices = prices;
	}

	public String toJson() {
		return JSONArray.fromObject(this).toString();
	}
}
