package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.cassandra.ds.CassandraKepSpaceFactory;
import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.utils.WeekdayUtil;

import com.datastax.driver.core.BoundStatement;
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

	private Map<String, BoundStatement> statementCache = new HashMap<String, BoundStatement>();

	protected IndMacdCassTableHelper(Session session) {
		this.session = session;
	}

	public BoundStatement getStatement(String cql) {
		BoundStatement bs = statementCache.get(cql);
		if (bs == null) {
			bs = new BoundStatement(session.prepare(cql));
			statementCache.put(cql, bs);
		}
		return bs;
	}

	public static IndMacdCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndMacdCassTableHelper(CassandraKepSpaceFactory.createCluster().connect());
		}
		return instance;
	}

	public void insert(MacdVO vo) {
		BoundStatement boundStatement = getStatement(INSERT_SQL);
		session.execute(boundStatement.bind(vo.stockId, vo.date, vo.dif, vo.dea, vo.macd));
	}

	public List<MacdVO> getAllMacd(String stockId) {
		List<MacdVO> list = new ArrayList<MacdVO>();
		BoundStatement boundStatement = getStatement(QUERY_ALL_BY_ID_SQL);
		ResultSet results = session.execute(boundStatement.bind(stockId));
		for (Row r : results.all()) {
			MacdVO vo = new MacdVO();
			vo.stockId = stockId;
			vo.date = r.getString("date");
			vo.dea = r.getDouble("dea");
			vo.dif = r.getDouble("dif");
			vo.macd = r.getDouble("macd");
			list.add(vo);
			System.out.println(vo);
		}
		return list;
	}

	public void delete(String stockId) {
		BoundStatement boundStatement = getStatement(DELETE_BY_STOCKID_SQL);
		session.execute(boundStatement.bind(stockId));
	}

	public void delete(String stockId, String date) {
		BoundStatement boundStatement = getStatement(DELETE_BY_STOCKID_AND_DATE_SQL);
		session.execute(boundStatement.bind(stockId, date));
	}

	public static void main(String[] args) {
		IndMacdCassTableHelper cable = IndMacdCassTableHelper.getInstance();

		for (int i = 0; i < 10; i++) {
			MacdVO vo = new MacdVO();
			vo.stockId = "000001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.dif = RandomUtils.nextDouble();
			vo.dea = RandomUtils.nextDouble();
			vo.macd = vo.dea - vo.dif;
			System.out.println("insert " + vo);
			cable.insert(vo);
		}

		cable.getAllMacd("000001");
		cable.delete("000001", "2017-11-15");
		cable.getAllMacd("000001");
	}
}
