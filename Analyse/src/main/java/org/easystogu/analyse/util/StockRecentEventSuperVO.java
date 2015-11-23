package org.easystogu.analyse.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.ZiJinLiuVO;

public class StockRecentEventSuperVO {
    public List<CheckPointDailySelectionVO> checkPoints = new ArrayList<CheckPointDailySelectionVO>(); //
    public Map<String, ZiJinLiuVO> ziJinLius = new HashMap<String, ZiJinLiuVO>();//_1Day, _3Day, _5Day zijinliu
}
