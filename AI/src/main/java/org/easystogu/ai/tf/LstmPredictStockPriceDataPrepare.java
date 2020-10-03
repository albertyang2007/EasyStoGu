package org.easystogu.ai.tf;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.file.CSVFileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// pls refer to:
// https://github.com/lyshello123/stock_predict_with_LSTM/blob/master/stock_predict_2.py
// python script is: TF_LSTM_Predict_StockPrice.py
@Component
public class LstmPredictStockPriceDataPrepare {
	private static QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTableHelper = QianFuQuanStockPriceTableHelper
			.getInstance();
	@Autowired
	private DBAccessFacdeFactory dBAccessFacdeFactory;
	protected IndicatorDBHelperIF shenXianTable = dBAccessFacdeFactory.getInstance(Constants.indShenXian);
	protected IndicatorDBHelperIF macdTable = dBAccessFacdeFactory.getInstance(Constants.indMacd);
	protected IndicatorDBHelperIF kdjTable = dBAccessFacdeFactory.getInstance(Constants.indKDJ);
	protected IndicatorDBHelperIF qsddTable = dBAccessFacdeFactory.getInstance(Constants.indQSDD);
	protected IndicatorDBHelperIF wrTable = dBAccessFacdeFactory.getInstance(Constants.indWR);

	private static String filePath = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/exampleData/";
	private static String csvHeader = "date,open,close,low,high,volume,change,h1,h2,h3,dea,dif,macd,k,d,j,qsddS,qsddM,qsddL,wrS,wrM,wrL,label1";

	private void prepareStockSimpleDateForLstm(String stockId) {
		List<StockPriceVO> spList = qianFuQuanStockPriceTableHelper.queryByStockId(stockId);
		List<ShenXianVO> sxList = shenXianTable.getAll(stockId);
		List<MacdVO> macdList = macdTable.getAll(stockId);
		List<KDJVO> kdjList = kdjTable.getAll(stockId);
		List<QSDDVO> qsddList = qsddTable.getAll(stockId);
		List<WRVO> wrList = wrTable.getAll(stockId);

		for (int index = 0; index < spList.size() - 1; index++) {
			StockPriceVO currVo = spList.get(index);
			StockPriceVO nextVo = spList.get(index + 1);
			currVo.nextClose = nextVo.close;
			currVo.nextHigh = nextVo.high;
			currVo.nextLow = nextVo.low;
		}

		List<String[]> contents = new ArrayList();
		for (int index = 0; index < spList.size(); index++) {
			contents.add(toCsvPredictString(spList.get(index), sxList.get(index), macdList.get(index),
					kdjList.get(index), qsddList.get(index), wrList.get(index)).split(","));
		}

		// write to csv file
		String fileName = filePath + stockId + "_stockPrice.csv";
		CSVFileHelper.write(fileName, csvHeader.split(","), contents);
	}

	public static String toCsvPredictString(StockPriceVO spvo, ShenXianVO sxvo, MacdVO macdvo, KDJVO kdjvo,
			QSDDVO qsddvo, WRVO wrvo) {
		StringBuffer sb = new StringBuffer();
		sb.append(spvo.date);
		// input
		sb.append("," + spvo.open);
		sb.append("," + spvo.close);
		sb.append("," + spvo.low);
		sb.append("," + spvo.high);
		sb.append("," + spvo.volume);
		sb.append("," + (spvo.close - spvo.lastClose) / spvo.lastClose);// change

		sb.append("," + sxvo.h1);
		sb.append("," + sxvo.h2);
		sb.append("," + sxvo.h3);

		sb.append("," + macdvo.dea);
		sb.append("," + macdvo.dif);
		sb.append("," + macdvo.macd);

		sb.append("," + kdjvo.k);
		sb.append("," + kdjvo.d);
		sb.append("," + kdjvo.j);

		sb.append("," + qsddvo.shoTerm);
		sb.append("," + qsddvo.midTerm);
		sb.append("," + qsddvo.lonTerm);

		sb.append("," + wrvo.shoTerm);
		sb.append("," + wrvo.midTerm);
		sb.append("," + wrvo.lonTerm);

		// output
		sb.append("," + spvo.nextClose);// label1
		// sb.append("," + vo.nextHigh);// label2
		return sb.toString();
	}

	public static void main(String[] args) {
		new LstmPredictStockPriceDataPrepare().prepareStockSimpleDateForLstm("600036");
	}

}
