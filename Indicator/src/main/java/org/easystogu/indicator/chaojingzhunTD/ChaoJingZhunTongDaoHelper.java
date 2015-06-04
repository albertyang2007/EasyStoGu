package org.easystogu.indicator.chaojingzhunTD;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

import bitzguild.jcollection.Doubles;
import bitzguild.jcollection.DoublesFunctions;
import bitzguild.jcollection.mutable.DoublesArray;

public class ChaoJingZhunTongDaoHelper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DoublesFunctions functions = new DoublesFunctions();
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("002673");

		DoublesArray vvs = new DoublesArray();
		for (StockPriceVO vo : list) {
			vvs.add(new Double((vo.close + vo.low + vo.high) / 3.0));
		}

		Doubles xma = DoublesFunctions.xma(vvs, vvs.size());

		System.out.println("xma len=" + xma.size());

		for (int i = 0; i < vvs.size(); i++) {
			System.out.println("i=" + xma.get(i));
		}
	}

}
