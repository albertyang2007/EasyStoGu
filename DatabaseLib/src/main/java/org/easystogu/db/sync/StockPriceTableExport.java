package org.easystogu.db.sync;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import net.sf.json.JSONArray;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

public class StockPriceTableExport {
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private FileConfigurationService config = FileConfigurationService.getInstance();
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
        ins.exportToFile("2016-03-21");
    }

}
