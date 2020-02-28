package org.easystogu.ai.prepare;

import java.util.ArrayList;
import java.util.List;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.CSVFileHelper;

public class LstmPredictStockPriceDataPrepare {
  private static QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTableHelper = QianFuQuanStockPriceTableHelper.getInstance();
  private static String filePath = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/exampleData/";
  private static String csvHeader = "index_code,date,open,close,low,high,volume,money,change,label";
  
  private static void prepareStockSimpleDateForLstm(String stockId) {
    List<StockPriceVO> spList = qianFuQuanStockPriceTableHelper.queryByStockId(stockId);
    
    for(int index = 0; index < spList.size() - 1; index++) {
      StockPriceVO currVo = spList.get(index);
      StockPriceVO nextVo = spList.get(index + 1);
      currVo.nextClose = nextVo.close;
    }
    
    List<String[]> contents = new ArrayList();
    for(StockPriceVO vo : spList) {
      contents.add(vo.toCsvPredictString().split(","));
    }
    
    //write to csv file
    CSVFileHelper.write(filePath + stockId + ".csv", csvHeader.split(","), contents);
  }
  public static void main(String[] args) {
    prepareStockSimpleDateForLstm("999999");
  }

}
