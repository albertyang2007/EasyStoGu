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
			Map<String, List<CheckPointDailySelectionVO>> oriMap, final Map<String, List<ZiJinLiuVO>> ziJinLius) {
		Map<String, List<CheckPointDailySelectionVO>> sortedMap = new LinkedHashMap<String, List<CheckPointDailySelectionVO>>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<String, List<CheckPointDailySelectionVO>>> entryList = new ArrayList<Map.Entry<String, List<CheckPointDailySelectionVO>>>(
					oriMap.entrySet());
			Collections.sort(entryList, new Comparator<Map.Entry<String, List<CheckPointDailySelectionVO>>>() {
				public int compare(Entry<String, List<CheckPointDailySelectionVO>> entry1,
						Entry<String, List<CheckPointDailySelectionVO>> entry2) {
					List<CheckPointDailySelectionVO> value1 = null;
					List<CheckPointDailySelectionVO> value2 = null;
					try {
						value1 = entry1.getValue();
						value2 = entry2.getValue();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					// count checkPoint number & ZiJinLiu number
					int s1 = value1.size() + countZiJinLiuVONumber(entry1.getKey(), ziJinLius);
					int s2 = value2.size() + countZiJinLiuVONumber(entry2.getKey(), ziJinLius);

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
}
