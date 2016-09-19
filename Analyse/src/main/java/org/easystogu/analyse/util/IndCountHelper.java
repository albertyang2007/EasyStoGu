package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.vo.table.StockSuperVO;

public class IndCountHelper {
	public static void countAvgWR(List<StockSuperVO> overList) {
		for (StockSuperVO superVO : overList) {
			superVO.avgWR = (superVO.wrVO.shoTerm + superVO.wrVO.midTerm + superVO.wrVO.lonTerm) / 3;
		}
	}
}
