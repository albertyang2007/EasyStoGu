package org.easystogu.vo;

public class RealTimeZiJinLiuVO {

	public String stockId;
	public double majorNetIn;
	public double majorNetPer;
	public double biggestNetIn;
	public double biggestNetPer;
	public double bigNetIn;
	public double bigNetPer;
	public double midNetIn;
	public double midNetPer;
	public double smallNetIn;
	public double smallNetPer;

	public RealTimeZiJinLiuVO(String id) {
		this.stockId = id;
	}

	public String toNetInString() {
		return "ziJinLiu" + this.majorNetIn + "," + this.biggestNetIn + ","
				+ this.bigNetIn + "," + this.midNetIn + "," + this.smallNetIn
				+ "]";
	}

	public String toNetPerString() {
		return "ziJinLiu [" + this.majorNetPer + "," + this.biggestNetPer + ","
				+ this.bigNetPer + "," + this.midNetPer + ","
				+ this.smallNetPer + "]";
	}

	public boolean isValidated() {
		if (this.majorNetIn == 0 || this.biggestNetIn == 0
				|| this.bigNetIn == 0 || this.midNetIn == 0
				|| this.smallNetIn == 0)
			return false;
		return true;
	}

}
