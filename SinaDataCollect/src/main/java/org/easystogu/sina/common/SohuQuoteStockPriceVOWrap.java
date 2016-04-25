package org.easystogu.sina.common;

import java.util.List;

public class SohuQuoteStockPriceVOWrap {
	public String status;
	public List<List<String>> hq;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<List<String>> getHq() {
		return hq;
	}
	public void setHq(List<List<String>> hq) {
		this.hq = hq;
	}
}
