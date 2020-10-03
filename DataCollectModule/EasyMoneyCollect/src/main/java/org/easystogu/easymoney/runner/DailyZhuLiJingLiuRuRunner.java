package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.ZhuLiJingLiuRuTableHelper;
import org.easystogu.db.vo.table.ZhuLiJingLiuRuVO;
import org.easystogu.easymoney.helper.DailyZhuLiJingLiuRuFatchDataHelper;
import org.easystogu.easymoney.helper.DailyZiJinLiuFatchDataHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//pai ming
@Component
public class DailyZhuLiJingLiuRuRunner implements Runnable {
	@Autowired
    private DBConfigurationService config;
    private DailyZhuLiJingLiuRuFatchDataHelper fatchDataHelper = new DailyZhuLiJingLiuRuFatchDataHelper();
    private ZhuLiJingLiuRuTableHelper zhuLiJingLiuRuTableHelper = ZhuLiJingLiuRuTableHelper.getInstance();
    private int toPage = 10;

    public DailyZhuLiJingLiuRuRunner() {
        this.toPage = config.getInt("real_Time_Get_ZiJin_Liu_PageNumber", 10);
    }

    public void resetToAllPage() {
        this.toPage = DailyZiJinLiuFatchDataHelper.totalPages;
    }

    public void countAndSaved() {
        System.out.println("Fatch ZhuLiJingLiuRu only toPage = " + toPage);
        List<ZhuLiJingLiuRuVO> list = fatchDataHelper.getAllStockIdsZiJinLiu(toPage);
        System.out.println("Total Fatch ZhuLiJingLiuRu size = " + list.size());
        
		// first clean today's zijinliu data
		if (list.size() > 0 && Strings.isNotEmpty(fatchDataHelper.currentDate))
			zhuLiJingLiuRuTableHelper.deleteByDate(fatchDataHelper.currentDate);
        
        for (ZhuLiJingLiuRuVO vo : list) {
            if (vo.isValidated()) {
                //zhuLiJingLiuRuTableHelper.delete(vo.stockId, vo.date);
                zhuLiJingLiuRuTableHelper.insert(vo);
                // System.out.println(vo.stockId + "=" + vo.name);
            }
        }
    }

    public void run() {
        // TODO Auto-generated method stub
        countAndSaved();
    }

    public static void main(String[] args) {
        DailyZhuLiJingLiuRuRunner runner = new DailyZhuLiJingLiuRuRunner();
        runner.run();
    }
}
