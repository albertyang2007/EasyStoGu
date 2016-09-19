package org.easystogu.db.access.table;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.db.vo.table.WRVO;

public class StockSuperVOHelper {

	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected IndDDXTableHelper ddxTable = IndDDXTableHelper.getInstance();
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected IndWRTableHelper wrTable = IndWRTableHelper.getInstance();

	// protected IndYiMengBSTableHelper ymbsTable =
	// IndYiMengBSTableHelper.getInstance();

	public List<StockSuperVO> getAllStockSuperVO(String stockId) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdTable.getAllMacd(stockId);
		List<KDJVO> kdjList = kdjTable.getAllKDJ(stockId);
		List<BollVO> bollList = bollTable.getAllBoll(stockId);
		List<ShenXianVO> shenXianList = shenXianTable.getAllShenXian(stockId);
		List<QSDDVO> qsddList = qsddTable.getAllQSDD(stockId);
		List<WRVO> wrList = wrTable.getAllWR(stockId);

		if ((spList.size() != macdList.size()) || (macdList.size() != kdjList.size())
				|| (kdjList.size() != spList.size()) || (bollList.size() != spList.size())
				|| (shenXianList.size() != spList.size()) || (qsddList.size() != spList.size())
				|| (wrList.size() != spList.size())) {
			return overList;
		}

		if ((spList.size() == 0) || (macdList.size() == 0) || (kdjList.size() == 0) || (bollList.size() == 0)
				|| (shenXianList.size() == 0) || (qsddList.size() == 0) || (wrList.size() == 0)) {
			return overList;
		}

		if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
				|| !spList.get(0).date.equals(bollList.get(0).date)
				|| !spList.get(0).date.equals(shenXianList.get(0).date)
				|| !spList.get(0).date.equals(qsddList.get(0).date) || !spList.get(0).date.equals(wrList.get(0).date)) {
			return overList;
		}

		for (int index = 0; index < spList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index),
					bollList.get(index));
			superVO.setShenXianVO(shenXianList.get(index));
			superVO.setWRVO(wrList.get(index));
			superVO.setDdxVO(ddxTable.getDDX(superVO.priceVO.stockId, superVO.priceVO.date));
			superVO.setQsddVO(qsddList.get(index));
			overList.add(superVO);
		}

		return overList;
	}

	public List<StockSuperVO> getLatestNStockSuperVO(String stockId, int day) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = qianFuQuanStockPriceTable.getNdateStockPriceById(stockId, day);
		List<MacdVO> macdList = macdTable.getNDateMacd(stockId, day);
		List<KDJVO> kdjList = kdjTable.getNDateKDJ(stockId, day);
		List<BollVO> bollList = bollTable.getNDateBoll(stockId, day);
		List<ShenXianVO> shenXianList = shenXianTable.getNDateShenXian(stockId, day);
		List<QSDDVO> qsddList = qsddTable.getNDateQSDD(stockId, day);
		// List<YiMengBSVO> ymbsList = ymbsTable.getNDateYiMengBS(stockId, day);

		if ((spList.size() != day) || (macdList.size() != day) || (kdjList.size() != day) || (bollList.size() != day)
				|| (shenXianList.size() != day) || (qsddList.size() != day)) {
			// System.out.println(stockId + " size of spList(" + spList.size() +
			// "), macdList(" + macdList.size()
			// + ") and kdjList(" + kdjList.size() + ") and xueShie2List(" +
			// xueShie2List.size()
			// + ") is not equal, the database must meet fatel error!");
			return overList;
		}

		if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
				|| !spList.get(0).date.equals(bollList.get(0).date)
				|| !spList.get(0).date.equals(shenXianList.get(0).date)
				|| !spList.get(0).date.equals(qsddList.get(0).date)) {
			// System.out.println(stockId
			// +
			// " date of spList, macdList and kdjList is not equal, the database
			// must meet fatel error!");
			return overList;
		}

		if (!spList.get(day - 1).date.equals(macdList.get(day - 1).date)
				|| !spList.get(day - 1).date.equals(kdjList.get(day - 1).date)
				|| !spList.get(day - 1).date.equals(bollList.get(day - 1).date)
				|| !spList.get(day - 1).date.equals(shenXianList.get(day - 1).date)
				|| !spList.get(day - 1).date.equals(qsddList.get(day - 1).date)) {
			// System.out.println(stockId + " Date of spList(" + spList.get(day
			// - 1).date + "), macdList("
			// + macdList.get(day - 1).date + "),kdjList(" + kdjList.get(day -
			// 1).date + "),bollList("
			// + bollList.get(day - 1).date + "),xueShie2List(" +
			// xueShie2List.get(day - 1).date
			// + ") is not equal, the database must meet fatel error!");
			return overList;
		}

		for (int index = 0; index < spList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index),
					bollList.get(index));
			superVO.setShenXianVO(shenXianList.get(index));
			superVO.setQsddVO(qsddList.get(index));
			// superVO.setYiMengBSVO(ymbsList.get(index));
			superVO.setDdxVO(ddxTable.getDDX(superVO.priceVO.stockId, superVO.priceVO.date));
			// superVO.setZhiJinLiuVO(ziJinLiuTableHelper.getZiJinLiu(superVO.priceVO.stockId,
			// superVO.priceVO.date));

			overList.add(superVO);
		}

		return overList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
