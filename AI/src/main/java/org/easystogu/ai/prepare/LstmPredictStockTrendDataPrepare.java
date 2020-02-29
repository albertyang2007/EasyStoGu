package org.easystogu.ai.prepare;

import java.util.ArrayList;
import java.util.List;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.CSVFileHelper;

public class LstmPredictStockTrendDataPrepare {
  private static QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTableHelper =
      QianFuQuanStockPriceTableHelper.getInstance();
  private static String filePath = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/exampleData/";
  private static String csvHeader = "index_code,date,open,close,low,high,volume,money,change,label1,label2";

  private static void prepareStockSimpleDateForLstm(String stockId) {
    List<StockPriceVO> spList = qianFuQuanStockPriceTableHelper.queryByStockId(stockId);

    for (int index = 0; index < spList.size() - 1; index++) {
      StockPriceVO currVo = spList.get(index);
      StockPriceVO nextVo = spList.get(index + 1);
      currVo.nextClose = nextVo.close;
    }

    List<String[]> contents = new ArrayList();
    for (StockPriceVO vo : spList) {
      contents.add(toCsvPredictString(vo).split(","));
    }

    // write to csv file
    CSVFileHelper.write(filePath + stockId + "_stockCheckPoint.csv", csvHeader.split(","), contents);
  }

  public static String toCsvPredictString(StockPriceVO vo) {
    StringBuffer sb = new StringBuffer();
    sb.append(vo.stockId);
    sb.append("," + vo.date);
    sb.append("," + vo.open);
    sb.append("," + vo.close);
    sb.append("," + vo.low);
    sb.append("," + vo.high);
    sb.append("," + vo.volume);
    sb.append("," + vo.volume);// there is no money, so use volume
    sb.append("," + (vo.close - vo.lastClose) / vo.close);// change
    sb.append("," + vo.nextClose);// label
    sb.append("," + vo.nextClose);// labe2
    return sb.toString();
  }

  public static void main(String[] args) {
    prepareStockSimpleDateForLstm("999999");
  }
}
