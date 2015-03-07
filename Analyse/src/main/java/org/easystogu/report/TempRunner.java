/*------------------------------------------------------------------------------
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *----------------------------------------------------------------------------*/
package org.easystogu.report;

import org.easystogu.checkpoint.DailyCombineCheckPoint;

public class TempRunner {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HistoryAnalyseReport reporter = new HistoryAnalyseReport();
        for (int length = 5; length <= 30; length++) {
            reporter.combineAanalyserHelper.tempInputArgs[0] = length;
            reporter.combineAanalyserHelper.tempInputArgs[1] = length;
            reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.DuoTou_MA5_Wait_MA10_RSV_KDJ_Gordon_Break_Platform);
            System.out.println("Platform length=" + length);
        }
    }

}
