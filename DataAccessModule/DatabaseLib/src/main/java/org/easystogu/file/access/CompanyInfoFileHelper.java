package org.easystogu.file.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.access.table.FavoritesStockHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.StringComparator;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// txt file to store all company base info
// export all data from easymoney software PC version in "分析"->"财务数据", select xls file format
// the Table_CompanyBaseInfo.xls is saved in CommonLib\src\main\resources
// than conver to Table_CompanyBaseInfo.csv format
@Component
public class CompanyInfoFileHelper {
  private static Logger logger = LogHelper.getLogger(CompanyInfoFileHelper.class);
  @Autowired
  private CompanyInfoTableHelper companyInfoTable;
  @Autowired
  protected TextFileSourceHelper fileSource;
  protected String fileName = "./Company_Info/Company_Info.csv";
  private static Map<String, CompanyInfoVO> companyMap = new HashMap<String, CompanyInfoVO>();
  
  @Autowired
  protected FavoritesStockHelper favoritesStockHelper;

  private void init() {
    //the env onlyUseFavoritesStocksInK8s shold be set in the application.properties
    //and config to configMap in K8s
    if("true".equalsIgnoreCase(System.getenv("onlyUseFavoritesStocksInK8s"))) {
      List<FavoritesStockVO> list = this.favoritesStockHelper.getByUserId("admin");
      for(FavoritesStockVO fstock : list) {
        CompanyInfoVO cInfo = companyInfoTable.getCompanyInfoByStockId(fstock.getStockId());
        if(cInfo != null) {
          companyMap.put(cInfo.stockId, cInfo);
        }
      }
    }else {
      //load all the company info from database
      this.loadDataFromDatabase();
    }

    // add special stockId and name
    CompanyInfoVO vo1 = new CompanyInfoVO("999999", "上证指数");
    companyMap.put(vo1.stockId, vo1);

    CompanyInfoVO vo2 = new CompanyInfoVO("399001", "深证成指");
    companyMap.put(vo2.stockId, vo2);

    CompanyInfoVO vo3 = new CompanyInfoVO("399006", "创业板指");
    companyMap.put(vo3.stockId, vo3);
  }

  // do not use this method now, since it need manually update
  private Map<String, CompanyInfoVO> loadDataFromFile() {
    Map<String, CompanyInfoVO> companyMapFile = new HashMap<String, CompanyInfoVO>();
    String content = fileSource.loadContent(fileName);
    if (Strings.isEmpty(content)) {
      System.err.println("fileName " + fileName + " content is null");
      return companyMapFile;
    }
    String[] lines = content.split("\n");
    for (int index = 1; index < lines.length; index++) {
      String line = lines[index];
      if (Strings.isNotEmpty(line)) {
        CompanyInfoVO vo = new CompanyInfoVO(line);
        companyMapFile.put(vo.stockId, vo);
        // System.out.println(vo);
      }
    }
    return companyMapFile;
  }

  private void loadDataFromDatabase() {
    logger.info("loading company info from database.");
    List<CompanyInfoVO> list = companyInfoTable.getAllCompanyInfo();
    for (CompanyInfoVO vo : list) {
      companyMap.put(vo.stockId, vo);
    }
  }

  public CompanyInfoVO getByStockId(String stockId) {
    if (companyMap.size() == 0) {
      this.init();
    }
    return this.companyMap.get(stockId);
  }

  public String getStockName(String stockId) {
    if (companyMap.size() == 0) {
      this.init();
    }

    CompanyInfoVO vo = this.companyMap.get(stockId);
    if (vo != null)
      return vo.name;

    return "N/A";
  }

  @SuppressWarnings("unchecked")
  public List<String> getAllStockId() {    
    if (companyMap.size() == 0) {
      this.init();
    }

    List<String> stockIds = new ArrayList<String>();
    Set<String> set = this.companyMap.keySet();
    stockIds.addAll(set);
    stockIds.add(getSZZSStockIdForDB());
    stockIds.add(getSZCZStockIdForDB());
    stockIds.add(getCYBZStockIdForDB());

    Collections.sort(stockIds, new StringComparator());

    return stockIds;
  }

  public List<String> getAllSZStockId(String prefix) {
    if (companyMap.size() == 0) {
      this.init();
    }

    List<String> stockIds = new ArrayList<String>();
    Set<String> set = this.companyMap.keySet();
    for (String stockId : set) {
      if (stockId.startsWith("0") || stockId.startsWith("3")) {
        stockIds.add(prefix + stockId);
      }
    }
    return stockIds;
  }

  // sina stockId mapping to DataBase
  // input is like: "sh000001" "sz000002" "sh600123"
  // return is like: 999999, 000002, 600123
  public String getStockIdMapping(String stockIdWithPrefix) {
    if (stockIdWithPrefix.equals(getSZZSStockIdForSina())) {
      return getSZZSStockIdForDB();
    } else if (stockIdWithPrefix.equals(getSZCZStockIdForSina())) {
      return getSZCZStockIdForDB();
    } else if (stockIdWithPrefix.equals(getCYBZStockIdForSina())) {
      return getCYBZStockIdForDB();
    }
    // stockId has prefix, so remove it (sh, sz)
    return stockIdWithPrefix.substring(2);
  }

  public List<String> getAllSHStockId(String prefix) {
    if (companyMap.size() == 0) {
      this.init();
    }

    List<String> stockIds = new ArrayList<String>();
    Set<String> set = this.companyMap.keySet();
    for (String stockId : set) {
      if (stockId.startsWith("6")) {
        stockIds.add(prefix + stockId);
      }
    }
    return stockIds;
  }

  public String getStockIdByName(String stockName) {
    if (companyMap.size() == 0) {
      this.init();
    }

    for (Map.Entry<String, CompanyInfoVO> entry : companyMap.entrySet()) {
      String stockId = entry.getKey().toString();
      String name = entry.getValue().name;
      if (name.equalsIgnoreCase(stockName)) {
        return stockId;
      }
    }
    return "";
  }

  public CompanyInfoVO getByStockName(String stockName) {
    if (companyMap.size() == 0) {
      this.init();
    }

    for (Map.Entry<String, CompanyInfoVO> entry : companyMap.entrySet()) {
      String name = entry.getValue().name;
      if (name.equalsIgnoreCase(stockName)) {
        return entry.getValue();
      }
    }
    return null;
  }

  // 上证指数
  public String getSZZSStockIdForSohu() {
    // szzs for search from
    // http://q.stock.sohu.com/hisHq?code=zs_000001&start=20000504&end=20160422&stat=1&order=D&period=d&rt=json
    return "000001";
  }

  // 上证指数
  public String getSZZSStockIdForSina() {
    // szzs for search from http://hq.sinajs.cn/list=sh000001
    return "sh000001";
  }

  // 深证成指
  public String getSZCZStockIdForSina() {
    // szzs for search from http://hq.sinajs.cn/list=sz399001
    return "sz399001";
  }

  // 创业板指
  public String getCYBZStockIdForSina() {
    // szzs for search from http://hq.sinajs.cn/list=sz399006
    return "sz399006";
  }

  // 上证指数
  public String getSZCZStockIdForDB() {
    // szzs for search from http://hq.sinajs.cn/list=sh000001
    return "399001";
  }

  // 深证成指
  public String getCYBZStockIdForDB() {
    // szzs for search from http://hq.sinajs.cn/list=sh000001
    return "399006";
  }

  // 创业板指
  public String getSZZSStockIdForDB() {
    // szzs for search from http://hq.sinajs.cn/list=sh000001
    return "999999";
  }

  public boolean isStockIdAMajorZhiShu(String stockId) {
    if (stockId.equals(getSZCZStockIdForDB()))
      return true;
    if (stockId.equals(getCYBZStockIdForDB()))
      return true;
    if (stockId.equals(getSZZSStockIdForDB()))
      return true;
    return false;
  }

  // update base company info to DB, add zhongguben, liutongguben
  // DDX use this data
  public void updateCompanyFromFileToDB() {
    // TODO Auto-generated method stub
    Map<String, CompanyInfoVO> companyMap = this.loadDataFromFile();

    Set keys = companyMap.keySet();
    Iterator it = keys.iterator();

    while (it.hasNext()) {
      String stockId = (String) it.next();
      CompanyInfoVO vo = (CompanyInfoVO) companyMap.get(stockId);
      // System.out.println(vo);
      if (vo.totalGuBen != 0 && vo.liuTongAGu != 0) {
        this.companyInfoTable.delete(vo.stockId);
        // System.out.println("Insert Company_Info :" + vo);
        this.companyInfoTable.insert(vo);
        // System.out.println(vo);
      }
    }
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    CompanyInfoFileHelper ins = new CompanyInfoFileHelper();
    // ins.companyInfoTable.delete("000001");
    ins.updateCompanyFromFileToDB();
  }
}
