package org.easystogu.db.vo.table;

//table name = "ind_xueshi2"
public class XueShi2VO  extends IndicatorVO{

	public String stockId;
	public String name;
	public String date;
	public double up;
	public double dn;

	public XueShi2VO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("XueShi2VO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", up:" + up);
		sb.append(", dn:" + dn);
		sb.append("}");
		return sb.toString();
	}

	public String toSimpleString() {
		StringBuffer sb = new StringBuffer("XueShi2VO: {");
		sb.append(", up:" + String.format("%.2f", up));
		sb.append(", dn:" + String.format("%.2f", dn));
		sb.append("}");
		return sb.toString();
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getUp() {
		return up;
	}

	public void setUp(double up) {
		this.up = up;
	}

	public double getDn() {
		return dn;
	}

	public void setDn(double dn) {
		this.dn = dn;
	}

}
