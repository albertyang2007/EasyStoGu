package org.easystogu.yahoo.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easystogu.yahoo.common.DayPriceVO;
import org.easystogu.yahoo.common.ResourceLoaderHelper;

//yahoo历史数据
//ichart.yahoo.com/table.csv?s=600388.ss&a=0&b=01&c=2014&d=11&e=16&f=2014&g=d
public class CSVReader {
	private ArrayList<DayPriceVO> allDataList = null;

	public CSVReader(String file) {
		getAllData(file);
	}

	public List<DayPriceVO> getAllData(String file) {
		allDataList = new ArrayList<DayPriceVO>();
		try {
			CSVParser parser = CSVParser.parse(
					ResourceLoaderHelper.loadAsFile(file),
					Charset.defaultCharset(), CSVFormat.EXCEL);
			for (CSVRecord record : parser) {
				if (record.getRecordNumber() > 1) {
					DayPriceVO vo = new DayPriceVO(record.iterator());
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
		for (DayPriceVO vo : allDataList) {
			rtnList.add(vo.close);
		}
		return rtnList;
	}

	public List<Double> getAllHightPrice() {
		List<Double> rtnList = new ArrayList<Double>();
		for (DayPriceVO vo : allDataList) {
			rtnList.add(vo.high);
		}
		return rtnList;
	}

	public List<Double> getAllLowPrice() {
		List<Double> rtnList = new ArrayList<Double>();
		for (DayPriceVO vo : allDataList) {
			rtnList.add(vo.low);
		}
		return rtnList;
	}

	public static void main(String[] args) {

	}
}
