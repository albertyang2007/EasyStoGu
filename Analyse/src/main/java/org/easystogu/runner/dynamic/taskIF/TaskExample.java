package org.easystogu.runner.dynamic.taskIF;

import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.runner.DataBaseSanityCheck;

public class TaskExample implements Runnable {
    public void run() {
        String taskName = "DataBaseSanityCheck for SZ";
        
        System.out.println("Dynamic Task start:" + taskName);

        CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        DataBaseSanityCheck check = new DataBaseSanityCheck();
        check.sanityDailyCheck(stockConfig.getAllSZStockId());
        check.sanityWeekCheck(stockConfig.getAllSZStockId());

        System.out.println("Dynamic Task stop:" + taskName);
    }
}
