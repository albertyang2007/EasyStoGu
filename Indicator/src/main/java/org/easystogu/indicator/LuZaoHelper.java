package org.easystogu.indicator;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.table.LuZaoVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.utils.Strings;

public class LuZaoHelper {
	public static List<LuZaoVO> countAvgMA(List<StockPriceVO> spList) {
		List<LuZaoVO> list = new ArrayList<LuZaoVO>();
		if (spList == null || spList.size() == 0)
			return list;

		for (int index = spList.size() - 1; index > 0; index--) {
			StockPriceVO spvo = spList.get(index);
			LuZaoVO lzvo = new LuZaoVO();
			lzvo.stockId = spvo.stockId;
			lzvo.name = spvo.name;
			lzvo.date = spvo.date;

			if (((index - 18) >= 0) && ((index + 1) <= spList.size())) {
				lzvo.ma19 = avgClosePrice(spList.subList(index - 18, index + 1));
			}
			if (((index - 42) >= 0) && ((index + 1) <= spList.size())) {
				lzvo.ma43 = avgClosePrice(spList.subList(index - 42, index + 1));
			}
			if (((index - 85) >= 0) && ((index + 1) <= spList.size())) {
				lzvo.ma86 = avgClosePrice(spList.subList(index - 85, index + 1));
			}
			list.add(lzvo);
		}
		return list;
	}

	public static double avgClosePrice(List<StockPriceVO> subList) {
		double avg = 0.0;
		for (StockPriceVO vo : subList) {
			avg += vo.close;
		}
		if (subList.size() > 0) {
			return Strings.convert2ScaleDecimal(avg / subList.size());
		}
		return 0.0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
