package org.easystogu.easymoney.common;

public class RealTimeZiJinLiuVO {
	public String stockId;
	public double majorNetIn;// 主力净流入
	public double biggestNetIn;// 超大单净流入
	public double bigNetIn;// 大单净流入
	public double midNetIn;// 中单净流入
	public double smallNetIn;// 小单净流入

	public RealTimeZiJinLiuVO(String id) {
		this.stockId = id;
	}

	public String toString() {
		return "ziJinLiuVO [" + this.stockId + "," + this.majorNetIn + "," + this.biggestNetIn + "," + this.bigNetIn
				+ "," + this.midNetIn + "," + this.smallNetIn + "]";
	}

	public boolean isValidated() {
		if (this.majorNetIn == 0 || this.biggestNetIn == 0 || this.bigNetIn == 0 || this.midNetIn == 0
				|| this.smallNetIn == 0)
			return false;
		return true;
	}
}
