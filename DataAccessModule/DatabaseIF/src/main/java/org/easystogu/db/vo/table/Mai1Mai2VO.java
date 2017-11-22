package org.easystogu.db.vo.table;
//table name = "ind_mai1mai2"
public class Mai1Mai2VO  extends IndicatorVO{

	public String stockId;
	public String date;
	public double sd;
	public double sk;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Mai1Mai2VO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Mai1Mai2VO: {");
		sb.append("stockId:" + stockId);
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
