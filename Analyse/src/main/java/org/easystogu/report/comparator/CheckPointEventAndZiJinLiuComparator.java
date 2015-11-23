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

public class CheckPointEventAndZiJinLiuComparator {
    public static Map<String, List<CheckPointDailySelectionVO>> sortMapByValue(
            Map<String, List<CheckPointDailySelectionVO>> oriMap) {
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
                    return value2.size() - value1.size();
                }
            });
            Iterator<Map.Entry<String, List<CheckPointDailySelectionVO>>> iter = entryList.iterator();
            Map.Entry<String, List<CheckPointDailySelectionVO>> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }
}
