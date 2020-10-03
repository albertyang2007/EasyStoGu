package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.db.access.table.IndDDXTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.ZiJinLiuTableHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.table.DDXVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.ZiJinLiuVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;

public class DailyDDXRunner implements Runnable {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private ZiJinLiuTableHelper zijinliuTableHelper = ZiJinLiuTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	@Autowired
	protected IndDDXTableHelper ddxTable;
	private String latestDate = stockPriceTable.getLatestStockDate();
	private int count = 0;

	public void countAndSaved(String stockId) {
		ZiJinLiuVO zjlvo = zijinliuTableHelper.getZiJinLiu(stockId, latestDate);
		if (zjlvo == null) {
			System.out.println("There is no ZiJinLiuVO for " + stockId + " at " + latestDate);
			return;
		}

		StockPriceVO spvo = stockPriceTable.getStockPriceByIdAndDate(stockId, latestDate);
		if (spvo == null) {
			System.out.println("There is no StockPriceVO for " + stockId + " at " + latestDate);
			return;
		}

		CompanyInfoVO civo = stockConfig.getByStockId(stockId);
		if (civo == null) {
			System.out.println("There is no CompanyInfoVO for " + stockId + " at " + latestDate);
			return;
		}

		if (civo.liuTongAGu <= 0) {
			System.out.println("LiuTongAGu is 0 for " + stockId + " at " + latestDate);
			return;
		}

		DDXVO ddxvo = new DDXVO();
		ddxvo.stockId = stockId;
		ddxvo.date = latestDate;
		ddxvo.ddx = Strings.convert2ScaleDecimal(zjlvo.getMajorNetIn() / (civo.liuTongAGu * spvo.close) / 100, 2);
		// System.out.println(ddxvo);
		ddxTable.insert(ddxvo);
		count++;
	}

	public void countAndSaved(List<String> stockIds) {
		System.out.println("Count ddx for all stockIds at date " + latestDate);
		ddxTable.deleteByDate(latestDate);
		for (String stockId : stockIds) {
			this.countAndSaved(stockId);
		}
		System.out.println("Total count DDX:" + count);
	}

	public void countAndSaved() {
		countAndSaved(stockConfig.getAllStockId());
	}

	public void run() {
		countAndSaved(stockConfig.getAllStockId());
	}

	public static void main(String[] args) {
		DailyDDXRunner runner = new DailyDDXRunner();
		runner.run();
	}
}
