package org.easystogu.db.table;

//table name = "ind_zhulijinchu"
public class ZhuliJinChuVO {

	public String stockId;
	public String name;
	public String date;
	public double duofang;
	public double kongfang;

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

	public ZhuliJinChuVO() {

	}

	public double[] getZhuliJinChuVO() {
		double[] duokong = new double[2];
		duokong[0] = this.duofang;
		duokong[1] = this.kongfang;

		return duokong;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ZhuliJinChuVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", duofang:" + duofang);
		sb.append(", kongfang:" + kongfang);
		sb.append("}");
		return sb.toString();
	}

	public double getDuofang() {
		return duofang;
	}

	public void setDuofang(double duofang) {
		this.duofang = duofang;
	}

	public double getKongfang() {
		return kongfang;
	}

	public void setKongfang(double kongfang) {
		this.kongfang = kongfang;
	}

}
