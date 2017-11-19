package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.cassandra.ds.CassandraKepSpaceFactory;
import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.utils.WeekdayUtil;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class IndMacdCassTableHelper {
	protected Session session;
	private static IndMacdCassTableHelper instance = null;
	// please modify this CQL in all subClass
	protected String tableName = Constants.CassandraKeySpace + ".ind_macd";
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, dif, dea, macd) VALUES (?, ?, ?, ?, ?)";
	protected String QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = ? ORDER BY date";
	protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = ?";
	protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = ? AND date = ?";

	protected IndMacdCassTableHelper(Session session) {
		this.session = session;
	}

	public static IndMacdCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndMacdCassTableHelper(CassandraKepSpaceFactory.createCluster().connect());
		}
		return instance;
	}

	public void insert(MacdVO vo) {
		PreparedStatement preparedStatement = session.prepare(INSERT_SQL);
		session.execute(preparedStatement.bind(vo.stockId, vo.date, vo.dif, vo.dea, vo.macd));
	}

	public void insert(List<MacdVO> list) {
		BatchStatement batchStatement = new BatchStatement(BatchStatement.Type.UNLOGGED);
		for (MacdVO vo : list) {
			PreparedStatement preparedStatement = session.prepare(INSERT_SQL);
			batchStatement.add(preparedStatement.bind(vo.stockId, vo.date, vo.dif, vo.dea, vo.macd));
		}
		session.execute(batchStatement);
	}

	public List<MacdVO> getAllMacd(String stockId) {
		List<MacdVO> list = new ArrayList<MacdVO>();
		PreparedStatement preparedStatement = session.prepare(QUERY_ALL_BY_ID_SQL);
		ResultSet results = session.execute(preparedStatement.bind(stockId));
		for (Row r : results.all()) {
			MacdVO vo = new MacdVO();
			vo.stockId = stockId;
			vo.date = r.getString("date");
			vo.dea = r.getDouble("dea");
			vo.dif = r.getDouble("dif");
			vo.macd = r.getDouble("macd");
			list.add(vo);
			//System.out.println(vo);
		}
		return list;
	}

	public void delete(String stockId) {
		PreparedStatement preparedStatement = session.prepare(DELETE_BY_STOCKID_SQL);
		session.execute(preparedStatement.bind(stockId));
	}

	public void delete(String stockId, String date) {
		PreparedStatement preparedStatement = session.prepare(DELETE_BY_STOCKID_AND_DATE_SQL);
		session.execute(preparedStatement.bind(stockId, date));
	}

	public static void main(String[] args) {
		IndMacdCassTableHelper cable = IndMacdCassTableHelper.getInstance();
		List<MacdVO> list = new ArrayList<MacdVO>();
		for (int i = 0; i < 10; i++) {
			MacdVO vo = new MacdVO();
			vo.stockId = "000001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.dif = RandomUtils.nextDouble();
			vo.dea = RandomUtils.nextDouble();
			vo.macd = vo.dea - vo.dif;
			list.add(vo);
		}

		cable.insert(list);
		cable.getAllMacd("000001");
		cable.delete("000001", "2017-11-19");
	}
}
