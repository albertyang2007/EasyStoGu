package org.easystogu.analyse.highlow;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;

//找出股价在19日，43日，86日内的最低最高价格和对应的日期
public class StockPriceHighLowFinder {
	private StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();

	public void findHighLowPriceInDays(String stockId, int day) {
		List<StockPriceVO> spList = qianFuQuanStockPriceTable.queryByStockId(stockId);
		int[] indexs = findHighPriceIndex(spList, day);
		for (int i = 0; i < indexs.length; i++) {
			System.out.println(i + ": " + indexs[i] + ": " + spList.get(i).toString());
		}
	}

	private int[] findHighPriceIndex(List<StockPriceVO> spList, int day) {
		int[] indexs = new int[spList.size()];
		for (int index = 0; index < spList.size() - day; index++) {
			double high = spList.get(index).high;
			List<StockPriceVO> subSpList = spList.subList(index, index + day);
			if (isHighestWithInList(high, subSpList)) {
				indexs[index] = 1;
			}
		}
		return indexs;
	}

	private boolean isHighestWithInList(double high, List<StockPriceVO> subSpList) {
		for (StockPriceVO vo : subSpList) {
			if (high < vo.high) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		StockPriceHighLowFinder ins = new StockPriceHighLowFinder();
		ins.findHighLowPriceInDays("601318", 19);
	}

}
