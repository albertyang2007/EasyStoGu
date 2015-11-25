package org.easystogu.report.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.ZiJinLiuVO;

public class CheckPointEventAndZiJinLiuComparator {
	public static Map<String, List<CheckPointDailySelectionVO>> sortMapByValue(
			Map<String, List<CheckPointDailySelectionVO>> oriMap, final Map<String, List<ZiJinLiuVO>> ziJinLius,
			final Map<String, Integer> liuTongShiZhis) {
		Map<String, List<CheckPointDailySelectionVO>> sortedMap = new LinkedHashMap<String, List<CheckPointDailySelectionVO>>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<String, List<CheckPointDailySelectionVO>>> entryList = new ArrayList<Map.Entry<String, List<CheckPointDailySelectionVO>>>(
					oriMap.entrySet());
			Collections.sort(entryList, new Comparator<Map.Entry<String, List<CheckPointDailySelectionVO>>>() {
				public int compare(Entry<String, List<CheckPointDailySelectionVO>> entry1,
						Entry<String, List<CheckPointDailySelectionVO>> entry2) {
					List<CheckPointDailySelectionVO> cpList1 = null;
					List<CheckPointDailySelectionVO> cpList2 = null;
					try {
						cpList1 = entry1.getValue();
						cpList2 = entry2.getValue();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					String stockId1 = entry1.getKey();
					String stockId2 = entry2.getKey();
					// count checkPoint number & ZiJinLiu number
					int s1 = cpList1.size() + countZiJinLiuVONumber(stockId1, ziJinLius) * 2
							+ countLiuTongShiZhi(stockId1, liuTongShiZhis);
					int s2 = cpList2.size() + countZiJinLiuVONumber(stockId2, ziJinLius) * 2
							+ countLiuTongShiZhi(stockId2, liuTongShiZhis);

					return s2 - s1;
				}
			});
			//
			Iterator<Map.Entry<String, List<CheckPointDailySelectionVO>>> iter = entryList.iterator();
			Map.Entry<String, List<CheckPointDailySelectionVO>> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}

	public static int countZiJinLiuVONumber(String stockId, Map<String, List<ZiJinLiuVO>> ziJinLius) {
		List<ZiJinLiuVO> list = ziJinLius.get(stockId);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	public static int countLiuTongShiZhi(String stockId, Map<String, Integer> liuTongShiZhis) {
		int liuTongShiZhi = liuTongShiZhis.get(stockId);
		if (liuTongShiZhi <= 50) {
			return 3;
		}
		if (liuTongShiZhi <= 100) {
			return 2;
		}
		if (liuTongShiZhi <= 200) {
			return 1;
		}
		return 0;
	}
}
