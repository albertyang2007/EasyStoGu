package org.easystogu.file.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.file.TextFileSourceHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;

//txt file to store all company base info
//export all data from easymoney software PC version in "分析"->"财务数据", select xls file format
//the Table_CompanyBaseInfo.xls is saved in CommonLib\src\main\resources
//chan conver to Table_CompanyBaseInfo.csv format
public class CompanyInfoFileHelper {
	private static Logger logger = LogHelper.getLogger(CompanyInfoFileHelper.class);
	private static CompanyInfoFileHelper instance = null;
	protected TextFileSourceHelper fileSource = TextFileSourceHelper.getInstance();
	protected String fileName = "Table_CompanyInfo.csv";
	private Map<String, CompanyInfoVO> companyMap = new HashMap<String, CompanyInfoVO>();

	public static CompanyInfoFileHelper getInstance() {
		if (instance == null) {
			instance = new CompanyInfoFileHelper();
		}
		return instance;
	}

	protected CompanyInfoFileHelper() {
		String[] lines = fileSource.loadContent(fileName);
		for (int index = 1; index < lines.length; index++) {
			String line = lines[index];
			if (Strings.isNotEmpty(line)) {
				CompanyInfoVO vo = new CompanyInfoVO(line);
				companyMap.put(vo.stockId, vo);
				// System.out.println(vo);
			}
		}
	}

	public CompanyInfoVO getByStockId(String stockId) {
		return this.companyMap.get(stockId);
	}

	public String getStockName(String stockId) {
		CompanyInfoVO vo = this.companyMap.get(stockId);
		if (vo != null) {
			return vo.name;
		} else if (stockId.equals(this.getSZZSStockIdForDB())) {
			return "上证指数";
		}

		return "N/A";
	}

	public List<String> getAllStockId() {
		List<String> stockIds = new ArrayList<String>();
		Set<String> set = this.companyMap.keySet();
		stockIds.addAll(set);
		stockIds.add(getSZZSStockIdForDB());
		return stockIds;
	}

	public List<String> getAllSZStockId() {
		List<String> stockIds = new ArrayList<String>();
		Set<String> set = this.companyMap.keySet();
		for (String stockId : set) {
			if (stockId.startsWith("0") || stockId.startsWith("3")) {
				stockIds.add(stockId);
			}
		}
		return stockIds;
	}

	public List<String> getAllSZStockId(String prefix) {
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
		}
		// stockId has prefix, so remove it (sh, sz)
		return stockIdWithPrefix.substring(2);
	}

	public List<String> getAllSHStockId() {
		List<String> stockIds = new ArrayList<String>();
		Set<String> set = this.companyMap.keySet();
		for (String stockId : set) {
			if (stockId.startsWith("6")) {
				stockIds.add(stockId);
			}
		}
		return stockIds;
	}

	public List<String> getAllSHStockId(String prefix) {
		List<String> stockIds = new ArrayList<String>();
		Set<String> set = this.companyMap.keySet();
		for (String stockId : set) {
			if (stockId.startsWith("6")) {
				stockIds.add(prefix + stockId);
			}
		}
		return stockIds;
	}

	public String getSZZSStockIdForSina() {
		// szzs for search from http://hq.sinajs.cn/list=sh000001
		return "sh000001";
	}

	public String getSZZSStockIdForDB() {
		// szzs for search from http://hq.sinajs.cn/list=sh000001
		return "999999";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
