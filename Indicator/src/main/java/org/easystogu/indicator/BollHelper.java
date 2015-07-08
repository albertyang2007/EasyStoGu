package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

public class BollHelper {

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("002194");
		TALIBWraper talib = new TALIBWraper();

		// list is order by date
		int length = list.size();
		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : list) {
			close[index++] = vo.close;
		}

		double[][] boll = talib.getBbands(close, 20, 2.0, 2.0);

		double up = boll[0][list.size() - 1];
		double mb = boll[1][list.size() - 1];
		double dn = boll[2][list.size() - 1];

		System.out.println("MB=" + mb);
		System.out.println("UP=" + up);
		System.out.println("DN=" + dn);
		// output:
		// MB=34.13050000000002
		// UP=37.170114942718456
		// DN=31.09088505728158
		// Refer:
		// MB=34.13
		// UP=37.17
		// DN=31.09
	}

}
