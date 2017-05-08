package org.easystogu.db.vo.view;

public class StatisticsViewVO {
	public String date;
	public int count;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String toString() {
		return this.date + ", " + this.count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
