package org.easystogu.vo;

public class RealTimeZiJinLiuVO {

	public String stockId;
	public double majorNetIn;// 主力净流入
	public double majorNetPer;// 主力净比
	public double biggestNetIn;// 超大单净流入
	public double biggestNetPer;// 超大单净比
	public double bigNetIn;// 大单净流入
	public double bigNetPer;// 大单净比
	public double midNetIn;// 中单净流入
	public double midNetPer;// 中单净比
	public double smallNetIn;// 小单净流入
	public double smallNetPer;// 小单净比

	public RealTimeZiJinLiuVO(String id) {
		this.stockId = id;
	}

	public String toNetInString() {
		return "ziJinLiu" + this.majorNetIn + "," + this.biggestNetIn + "," + this.bigNetIn + "," + this.midNetIn + ","
				+ this.smallNetIn + "]";
	}

	public String toNetPerString() {
		return "ziJinLiu [" + this.majorNetPer + "," + this.biggestNetPer + "," + this.bigNetPer + "," + this.midNetPer
				+ "," + this.smallNetPer + "]";
	}

	public boolean isValidated() {
		if (this.majorNetIn == 0 || this.biggestNetIn == 0 || this.bigNetIn == 0 || this.midNetIn == 0
				|| this.smallNetIn == 0)
			return false;
		return true;
	}

}
