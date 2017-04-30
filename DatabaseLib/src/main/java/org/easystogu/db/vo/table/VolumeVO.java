package org.easystogu.db.vo.table;

public class VolumeVO {
    public String stockId;
    public String name;
    public String date;
    public long volume;
    public long maVolume;
    public long hhvVolume;
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
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public long getMaVolume() {
		return maVolume;
	}
	public void setMaVolume(long maVolume) {
		this.maVolume = maVolume;
	}
	public long getHhvVolume() {
		return hhvVolume;
	}
	public void setHhvVolume(long hhvVolume) {
		this.hhvVolume = hhvVolume;
	}
}
