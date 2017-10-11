package org.easystogu.runner.dynamic.taskIF;

import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.runner.DataBaseSanityCheck;

public class TaskExample implements Runnable {
    public void run() {
        System.out.println("Hello, this is in run!!!");

        CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        DataBaseSanityCheck check = new DataBaseSanityCheck();
        check.sanityDailyCheck(stockConfig.getAllSZStockId());
        check.sanityWeekCheck(stockConfig.getAllSZStockId());
    }
}
