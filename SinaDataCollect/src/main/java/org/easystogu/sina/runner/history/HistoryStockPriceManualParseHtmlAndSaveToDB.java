package org.easystogu.sina.runner.history;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

//读取某个目录下面的sina html季度数据并且入库
public class HistoryStockPriceManualParseHtmlAndSaveToDB {
    private static Logger logger = LogHelper.getLogger(HistoryStockPriceManualParseHtmlAndSaveToDB.class);
    private StockPriceTableHelper tableHelper = StockPriceTableHelper.getInstance();
    private List<String> errorParseList = new ArrayList<String>();
    private List<String> errorDBList = new ArrayList<String>();

    public void parseDataAndSaveToDB(File file, String startDay) {
        List<StockPriceVO> list = this.parseStockPriceFromFile(file);
        for (StockPriceVO vo : list) {
            if (vo.date.compareTo(startDay) >= 0) {
                this.saveIntoDB(vo);
            }
        }
    }

    private List<StockPriceVO> parseStockPriceFromFile(File file) {
        List<StockPriceVO> list = new ArrayList<StockPriceVO>();
        String stockId = file.getName().split("_")[0];
        try {
            Document doc = Jsoup.parse(file, "UTF-8");
            Element element = doc.getElementById("FundHoldSharesTable");
            if (element == null) {
                logger.debug("Data is empty for " + file.getName());
                System.out.println("Data is empty for " + file.getName());
                return list;
            }
            Elements titleName = element.select("tr");
            for (int index = 2; index < titleName.size(); index++) {
                Element name = titleName.get(index);
                if (Strings.isEmpty(name.text().trim())) {
                    continue;
                }
                StockPriceVO vo = new StockPriceVO(name.text().trim());
                vo.stockId = stockId;
                // System.out.println(vo);
                list.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorParseList.add(new String(stockId));
            logger.error("Can not parse stock file " + file.getName(), e);
        }
        return list;
    }

    private void saveIntoDB(StockPriceVO vo) {
        try {
            // save to DB if not exist
            if (vo.isValidated()) {
                // System.out.println("saving into DB, vo=" + vo);
                if (tableHelper.getStockPriceByIdAndDate(vo.stockId, vo.date) == null) {
                    System.out.println("saving into DB, vo=" + vo);
                    tableHelper.insert(vo);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Can't save to DB, vo=" + vo + ", error=" + e.getMessage());
            e.printStackTrace();
            errorDBList.add(new String(vo.stockId));
            logger.error("Can not save stock price to DB " + vo.toString(), e);
        }
    }

    public void reportResult() {
        System.out.println("Total Parse Error: " + this.errorParseList.size());
        System.out.println("Total DB Error: " + this.errorDBList.size());
    }

    public static void main(String[] args) {
        // ��ĳĿ¼�µ��ļ�����������⣬ֻ����һ��
        FileConfigurationService fileConfile = FileConfigurationService.getInstance();
        HistoryStockPriceManualParseHtmlAndSaveToDB runner = new HistoryStockPriceManualParseHtmlAndSaveToDB();
        String htmlPath = fileConfile.getString("sina.history.file.path");
        File path = new File(htmlPath);
        File[] files = path.listFiles();
        int index = 0;
        String startDay = "1991-01-01";
        for (File file : files) {
            if (file.getName().contains("999999") || file.getName().contains("399001")
                    || file.getName().contains("399006")) {
                System.out.println("Processing " + file.getName() + " " + ++index + " / " + files.length);
                // please fliter the data that save into DB
                if ((file.length() > 0)) {
                    // save to DB if not exist
                    runner.parseDataAndSaveToDB(file, startDay);
                }
            }
        }

        runner.reportResult();
    }
}
