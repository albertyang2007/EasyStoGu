package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

import bitzguild.jcollection.Doubles;
import bitzguild.jcollection.DoublesFunctions;
import bitzguild.jcollection.mutable.DoublesArray;
//
public class ChaoJingZhunTongDaoHelper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DoublesFunctions functions = new DoublesFunctions();
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("002673");

		DoublesArray vvs = new DoublesArray();
		for (int i = list.size() - 30; i < list.size(); i++) {
			StockPriceVO vo = list.get(i);
			vvs.add(new Double((vo.close + vo.low + vo.high) / 3.0));
		}

		Doubles xma = DoublesFunctions.xma(vvs, 3);

		System.out.println("xma len=" + xma.size());

		System.out.println("i=" + xma.get(0));
		// System.out.println("i=" + xma.get(1));
	}

}
