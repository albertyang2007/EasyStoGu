package org.easystogu.database.replicate.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;

@Component
public class StockPriceTableExport {
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    @Autowired
    private DBConfigurationService config;
    private String stockPriceFilePath = config.getString("stockPrice.import_export.file.path");

    public void exportToFile(String date) {
        List<StockPriceVO> spList = stockPriceTable.getAllStockPriceByDate(date);

        String file = stockPriceFilePath + "/" + date + ".json";
        System.out.println("Saving stockPrice to " + file);
        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(file));
            fout.write(JSONArray.fromObject(spList).toString());
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockPriceTableExport ins = new StockPriceTableExport();
        ins.exportToFile("2016-04-05");
    }

}
