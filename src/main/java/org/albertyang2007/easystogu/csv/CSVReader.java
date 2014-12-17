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

    public List<DayPriceVO> getAllData(String file) {
        ArrayList<DayPriceVO> list = new ArrayList<DayPriceVO>();
        try {
            CSVParser parser = CSVParser.parse(ResourceLoaderHelper.loadAsFile(file), Charset.defaultCharset(),
                    CSVFormat.EXCEL);
            for (CSVRecord record : parser) {
                if (record.getRecordNumber() > 1) {
                    DayPriceVO vo = new DayPriceVO(record.iterator());
                    //System.out.println(vo);
                    if (vo.isValidated()) {
                        list.add(vo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Double> getAllClosedPrice(String file) {
        List<DayPriceVO> list = this.getAllData(file);
        List<Double> rtnList = new ArrayList<Double>();
        for (DayPriceVO vo : list) {
            rtnList.add(vo.close);
        }
        return rtnList;
    }

    public static void main(String[] args) {
        String csvFilePath = "classpath:/000821.csv";
        CSVReader ins = new CSVReader();
        ins.getAllData(csvFilePath);
    }

}
