package org.easystogu.db.table;

public class ScheduleActionVO {

	public static enum ActionDo {
		refresh_hou_fuquan_history_stockprice
	}

	public String stockId;
	public String runDate;
	public String createDate;
	public String actionDo;
	public String params;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getActionDo() {
		return actionDo;
	}

	public void setActionDo(String action) {
		this.actionDo = action;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
