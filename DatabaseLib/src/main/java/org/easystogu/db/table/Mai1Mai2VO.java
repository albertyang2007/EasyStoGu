package org.easystogu.db.table;

public class Mai1Mai2VO {

	public String stockId;
	public String name;
	public String date;
	public double sd;
	public double sk;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Mai1Mai2VO() {

	}

	public double[] getMai1Mai2VO() {
		double[] sdsk = new double[2];
		sdsk[0] = this.sd;
		sdsk[1] = this.sk;

		return sdsk;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Mai1Mai2VO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", sd:" + sd);
		sb.append(", sk:" + sk);
		sb.append("}");
		return sb.toString();
	}

	public double getSd() {
		return sd;
	}

	public void setSd(double sd) {
		this.sd = sd;
	}

	public double getSk() {
		return sk;
	}

	public void setSk(double sk) {
		this.sk = sk;
	}

}
