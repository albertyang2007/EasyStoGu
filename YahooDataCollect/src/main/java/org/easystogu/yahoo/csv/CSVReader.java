package org.easystogu.yahoo.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easystogu.db.table.StockPriceVO;

//yahoo��ʷ���
//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
public class CSVReader {
	private ArrayList<StockPriceVO> allDataList = null;

	public CSVReader(String file) {
		extractData(file);
	}

	private List<StockPriceVO> extractData(String file) {
		allDataList = new ArrayList<StockPriceVO>();
		try {
			CSVParser parser = CSVParser.parse(
					ResourceLoaderHelper.loadResourceAsFile(file),
					Charset.defaultCharset(), CSVFormat.EXCEL);
			for (CSVRecord record : parser) {
				if (record.getRecordNumber() > 1) {
					StockPriceVO vo = new StockPriceVO(record.iterator());
					// System.out.println(vo);
					if (vo.isValidated()) {
						allDataList.add(vo);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allDataList;
	}

	public List<Double> getAllClosedPrice() {
		List<Double> rtnList = new ArrayList<Double>();
		for (StockPriceVO vo : allDataList) {
			rtnList.add(vo.close);
		}
		return rtnList;
	}

	public List<Double> getAllHightPrice() {
		List<Double> rtnList = new ArrayList<Double>();
		for (StockPriceVO vo : allDataList) {
			rtnList.add(vo.high);
		}
		return rtnList;
	}

	public List<Double> getAllLowPrice() {
		List<Double> rtnList = new ArrayList<Double>();
		for (StockPriceVO vo : allDataList) {
			rtnList.add(vo.low);
		}
		return rtnList;
	}

	public List<StockPriceVO> getAllDataList() {
		return this.allDataList;
	}

	public static void main(String[] args) {

	}
}
