package org.albertyang2007.easystogu.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.albertyang2007.easystogu.common.DayPriceVO;
import org.albertyang2007.easystogu.common.ResourceLoaderHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {
    private ArrayList<DayPriceVO> allDataList = null;

    public CSVReader(String file) {
        getAllData(file);
    }

    public List<DayPriceVO> getAllData(String file) {
        allDataList = new ArrayList<DayPriceVO>();
        try {
            CSVParser parser = CSVParser.parse(ResourceLoaderHelper.loadAsFile(file), Charset.defaultCharset(),
                    CSVFormat.EXCEL);
            for (CSVRecord record : parser) {
                if (record.getRecordNumber() > 1) {
                    DayPriceVO vo = new DayPriceVO(record.iterator());
                    //System.out.println(vo);
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
