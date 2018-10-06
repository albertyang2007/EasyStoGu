package org.easystogu.db.vo.view;

public class FavoritesStockVO {
	public String stockId;
	public String name;
	public String userId;

	public FavoritesStockVO() {

	}

	public FavoritesStockVO(String stockId, String userId) {
		this.stockId = stockId;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return this.stockId + " " + this.name;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
