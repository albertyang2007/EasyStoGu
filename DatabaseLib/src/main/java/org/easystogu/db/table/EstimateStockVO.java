package org.easystogu.db.table;

public class EstimateStockVO {
	public String stockId;
	public String date;
	
	public EstimateStockVO(){
		
	}

	public EstimateStockVO(String _stockId, String _date) {
		this.stockId = _stockId;
		this.date = _date;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockid) {
		this.stockId = stockid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
