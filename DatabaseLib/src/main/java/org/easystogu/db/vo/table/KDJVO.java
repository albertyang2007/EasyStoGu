package org.easystogu.db.vo.table;

//table name = "ind_kdj"
public class KDJVO {

	public String stockId;
	public String name;
	public String date;
	public double k;
	public double d;
	public double j;
	public double rsv;

	public KDJVO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("KDJVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
        sb.append(", date:" + date);
        sb.append(", R:" + rsv);
		sb.append(", K:" + k);
		sb.append(", D:" + d);
		sb.append(", J:" + j);
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

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getJ() {
		return j;
	}

	public void setJ(double j) {
		this.j = j;
	}

	public double getRsv() {
		return rsv;
	}

	public void setRsv(double rsv) {
		this.rsv = rsv;
	}

	public boolean rsvValueBetween(double min, double max) {
		if ((this.rsv >= min) && (this.rsv <= max)) {
			return true;
		}
		return false;
	}

	public boolean kValueBetween(double min, double max) {
		if ((this.k >= min) && (this.k <= max)) {
			return true;
		}
		return false;
	}

	public boolean dValueBetween(double min, double max) {
		if ((this.d >= min) && (this.d <= max)) {
			return true;
		}
		return false;
	}

	public boolean jValueBetween(double min, double max) {
		if ((this.j >= min) && (this.j <= max)) {
			return true;
		}
		return false;
	}

}
